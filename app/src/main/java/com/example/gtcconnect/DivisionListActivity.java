package com.example.gtcconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DivisionListActivity extends AppCompatActivity {

    private ListView divisionListView;
    private ProgressBar progressBar;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.division_list); // Ensure your layout is correctly referenced

        // Initialize views
        divisionListView = findViewById(R.id.listView); // Assuming listView ID is correct
        progressBar = findViewById(R.id.progressBar); // Add a progress bar in the layout for better UX

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.68.71:3000/") // Replace with your backend's base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        // Fetch divisions
        fetchDivisions();
    }

    private void fetchDivisions() {
        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);

        Call<List<String>> call = retrofitInterface.getDivisions();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                // Hide progress bar
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<String> divisions = response.body();

                    // Set custom adapter for displaying the divisions
                    DivisionAdapter adapter = new DivisionAdapter(DivisionListActivity.this, divisions);
                    divisionListView.setAdapter(adapter);

                    // Handle click event on division name
                    divisionListView.setOnItemClickListener((parent, view, position, id) -> {
                        String selectedDivision = divisions.get(position);
                        // Pass the selected division to the next activity (ClubListActivity)
                        Intent intent = new Intent(DivisionListActivity.this, ClubListActivity.class);
                        intent.putExtra("divisionName", selectedDivision);
                        startActivity(intent);
                    });
                } else {
                    Toast.makeText(DivisionListActivity.this, "Failed to load divisions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                // Hide progress bar
                progressBar.setVisibility(View.GONE);

                // Log and notify the user of the error
                Log.e("DivisionListActivity", "Error: " + t.getMessage(), t);
                Toast.makeText(DivisionListActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Highlight the Discover menu item
        bottomNavigationView.setSelectedItemId(R.id.nav_clubs);

        // Set up navigation logic
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(DivisionListActivity.this, FeedPage.class));
                return true;
            } else if (id == R.id.nav_discover) {
                startActivity(new Intent(DivisionListActivity.this, Event_list.class));
                return true;
            } else if (id == R.id.nav_clubs) {
                return true;
            }
            return false;
        });
    }
}
