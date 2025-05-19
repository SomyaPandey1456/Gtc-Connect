package com.example.gtcconnect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

public class EventDetailsActivity extends AppCompatActivity {
    private static final String TAG = "EventDetailsActivity";

    private ImageView clubLogo, eventImage;
    private TextView clubName, eventTitle, eventDate, eventTime, aboutText, updatesText, queriesText;
    private Button registerButton;

    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        // Initialize views
        clubLogo = findViewById(R.id.clublogo);
        clubName = findViewById(R.id.clubname);
        eventTitle = findViewById(R.id.main_event_title);
        eventImage = findViewById(R.id.main_event_image);
        eventDate = findViewById(R.id.event_date_value);
        eventTime = findViewById(R.id.event_time_value);
        aboutText = findViewById(R.id.main_about_text);
        updatesText = findViewById(R.id.main_updates_text);
        queriesText = findViewById(R.id.main_queries_text);
        registerButton = findViewById(R.id.main_register_button);

        // Get event ID from Intent
        eventId = getIntent().getStringExtra("event_id");
        if (eventId == null) {
            Toast.makeText(this, "No Event ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Log.d(TAG,"Event Id : "+eventId);

        // Fetch event details
        fetchEventDetails(eventId);

        // Register button click listener
        registerButton.setOnClickListener(view -> {
            Toast.makeText(this, "Registration feature not implemented yet.", Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchEventDetails(String eventId) {
        String url = "http://192.168.68.71:3000/events/get/" + eventId; // Replace with your actual API endpoint
        Log.d(TAG, "Request URL: " + url);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse event details
                            JSONObject eventObject = response.getJSONObject("event");
                            String eventName = eventObject.optString("eventName", "N/A");
                            String eventImageUrl = eventObject.optString("eventPoster", "");
                            String eventDateStr = eventObject.optString("eventDate", "N/A");
                            String eventTimeStr = eventObject.optString("eventTime", "N/A");
                            String about = eventObject.optString("eventDescription", "Details not available.");

                            // Parse club details
                            JSONObject clubObject = response.getJSONObject("club");
                            String clubNameStr = clubObject.optString("clubName", "N/A");
                            String clubLogoUrl = clubObject.optString("clubLogo", "");

                            // Populate views with data
                            clubName.setText(clubNameStr);
                            Glide.with(EventDetailsActivity.this).load(clubLogoUrl)
                                    .placeholder(R.drawable.gtc).error(R.drawable.gtc).into(clubLogo);
                            eventTitle.setText(eventName);
                            Glide.with(EventDetailsActivity.this).load(eventImageUrl)
                                    .placeholder(R.drawable.gtc).error(R.drawable.gtc).into(eventImage);
                            eventDate.setText(eventDateStr);
                            eventTime.setText(eventTimeStr);
                            aboutText.setText(about);
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing event details: " + e.getMessage());
                            Toast.makeText(EventDetailsActivity.this, "Error loading event details", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error fetching event details: " + error.getMessage());
                        Toast.makeText(EventDetailsActivity.this, "Failed to load event details", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }
}
