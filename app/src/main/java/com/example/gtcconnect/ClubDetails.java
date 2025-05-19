package com.example.gtcconnect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

public class ClubDetails extends AppCompatActivity {

    private ImageView clubLogo;
    private TextView clubName;
    private Button aboutBtn, eventBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_details);

        // Initialize views
        clubLogo = findViewById(R.id.clublogo);
        clubName = findViewById(R.id.clubname);
        aboutBtn = findViewById(R.id.aboutbtn);
        eventBtn = findViewById(R.id.eventbtn);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);


        // Get clubId from intent
        String clubId = getIntent().getStringExtra("clubId");

        if (clubId != null) {
            fetchClubDetails(clubId);
        } else {
            Toast.makeText(this, "Club ID is missing!", Toast.LENGTH_SHORT).show();
        }

        loadFragment(new aboutclub());

        aboutBtn.setOnClickListener(v -> loadFragment(new aboutclub()));

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ClubDetails.this, addEvent.class);
            intent.putExtra("clubId", clubId); // Replace clubId with the actual variable holding the club's ID
            startActivity(intent);
        });
    }

    private void fetchClubDetails(String clubId) {
        // API URL for fetching club details
        String url = "http://192.168.68.71:3000/club/details/" + clubId;
        Log.d("ClubDetailsActivity", "clubId: " + clubId);
        Log.d("ClubDetailsActivity", "Request URL: " + url);

        // Initialize Volley RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create a GET request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse and display the data
                        try {
                            String name = response.optString("club_name", "N/A");
                            String logoUrl = response.optString("club_image", "");


                            Glide.with(ClubDetails.this)
                                    .load(logoUrl)
                                    .into(clubLogo);

                            clubName.setText(name);

                        } catch (Exception e) {
                            Toast.makeText(ClubDetails.this, "Error parsing club details", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ClubDetails.this, "Error fetching club details", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        queue.add(jsonObjectRequest);
    }

    private void loadFragment(Fragment fragment) {
        if (fragment instanceof aboutclub) {
            Bundle args = new Bundle();
            String clubId = getIntent().getStringExtra("clubId");
            args.putString("clubId", clubId);
            fragment.setArguments(args);
        }
        // Load the specified fragment into the FrameLayout
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.clubfragment, fragment);
        transaction.commit();
    }
}
