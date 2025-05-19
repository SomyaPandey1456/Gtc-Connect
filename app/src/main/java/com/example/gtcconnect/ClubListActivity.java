package com.example.gtcconnect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClubListActivity extends AppCompatActivity {
    private ListView clubListView;
    private ProgressBar progressBar;
    private List<Club> clubList;
    private ClubAdapter clubAdapter;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clubpage);

        // Initialize views
        clubListView = findViewById(R.id.clubList);
        progressBar = findViewById(R.id.progressBar);

        // Initialize list and adapter
        clubList = new ArrayList<>();
        clubAdapter = new ClubAdapter(this, clubList);
        clubListView.setAdapter(clubAdapter);

        // Get the division name passed from previous activity
        String divisionName = getIntent().getStringExtra("divisionName");

        // Initialize Retrofit and create interface
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.68.71:3000/") // Replace with your backend URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        // Fetch clubs by division
        fetchClubs(divisionName);
    }

    private void fetchClubs(String divisionName) {
        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Log division name for debugging
        Log.d("ClubListActivity", "Fetching clubs for division: " + divisionName);

        if (divisionName == null || divisionName.isEmpty()) {
            Toast.makeText(this, "Invalid division name", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String encodedDivisionName = URLEncoder.encode(divisionName, "UTF-8");
            Call<List<Club>> call = retrofitInterface.getClubsByDivision(encodedDivisionName);
            call.enqueue(new Callback<List<Club>>() {

                @Override
                public void onResponse(Call<List<Club>> call, Response<List<Club>> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        List<Club> clubs = response.body();
                        Log.d("ClubListActivity", "Received clubs: " + clubs.toString());

                        // Update your adapter
                        clubList.clear();
                        clubList.addAll(clubs);
                        clubAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("ClubListActivity", "Failed to get clubs: " + response.message());
                    }
                }


                @Override
                public void onFailure(Call<List<Club>> call, Throwable t) {
                    // Hide progress bar
                    progressBar.setVisibility(View.GONE);

                    // Log the error and show a message to the user
                    Log.e("ClubListActivity", "Error fetching clubs", t);
                    Toast.makeText(ClubListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }
}
