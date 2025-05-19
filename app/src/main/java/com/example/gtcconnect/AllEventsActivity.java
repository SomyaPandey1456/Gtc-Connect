package com.example.gtcconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AllEventsActivity extends AppCompatActivity {

    private List<Event> allEvents = new ArrayList<>(); // List to store all events
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_all);

        // Initialize views
        recyclerView = findViewById(R.id.listViewEvents);
        Button btnPast = findViewById(R.id.btnPrevious);
        Button btnPresent = findViewById(R.id.btnCurrent);
        Button btnUpcoming = findViewById(R.id.btnFuture);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(this, allEvents, event -> {
            // OnClick: Navigate to EventDetailsActivity with the event ID
            Intent intent = new Intent(AllEventsActivity.this, EventDetailsActivity.class);
            String eventId = event.getId();
            intent.putExtra("event_id", eventId); // Pass event ID
            Log.d("AllEvents", "Event id: "+ eventId);
            startActivity(intent);
        });
        recyclerView.setAdapter(eventAdapter);

        // Fetch event list from backend
        fetchEventList();

        // Filter buttons click listeners
        btnPast.setOnClickListener(v -> filterEvents("past"));
        btnPresent.setOnClickListener(v -> filterEvents("present"));
        btnUpcoming.setOnClickListener(v -> filterEvents("upcoming"));
    }

    // Fetch event list from backend
    private void fetchEventList() {
        String url = "http://192.168.68.71:3000/events/getall";
        Log.d("AllEventsActivity", "Request URL: " + url);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            allEvents.clear(); // Clear any existing data
                            JSONArray eventsArray = response.getJSONArray("events");

                            for (int i = 0; i < eventsArray.length(); i++) {
                                JSONObject eventObject = eventsArray.getJSONObject(i);

                                String id = eventObject.optString("_id", ""); // Fetch event ID
                                String title = eventObject.optString("eventName", "N/A");
                                String eventIconUrl = eventObject.optString("eventPoster", null);
                                String venue = eventObject.optString("eventVenue", "N/A");
                                String description = eventObject.optString("eventDescription", "N/A");
                                String type = eventObject.optString("eventType", "N/A");

                                String dateStr = eventObject.optString("eventDate", "N/A");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = null;
                                try {
                                    date = sdf.parse(dateStr);
                                } catch (ParseException e) {
                                    Log.e("AllEventsActivity", "Error parsing date: " + e.getMessage());
                                }

                                // Add event to the list
                                allEvents.add(new Event(id, title, eventIconUrl, venue, description, type, date));
                            }

                            // Notify adapter of new data
                            eventAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            Log.e("AllEventsActivity", "Error parsing JSON response: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AllEventsActivity", "Error fetching event list: " + error.getMessage());
                    }
                });

        queue.add(jsonObjectRequest);
    }

    // Filter events based on the type
    private void filterEvents(String type) {
        List<Event> filteredEvents = new ArrayList<>();
        Date currentDate = new Date();

        for (Event event : allEvents) {
            Date eventDate = event.getDate();
            if (eventDate == null) continue;

            if (type.equals("past") && eventDate.before(currentDate)) {
                filteredEvents.add(event);
            } else if (type.equals("present")) {
                long diff = eventDate.getTime() - currentDate.getTime();
                long daysDiff = diff / (1000 * 60 * 60 * 24);
                if (daysDiff >= 0 && daysDiff <= 5) { // Within 5 days
                    filteredEvents.add(event);
                }
            } else if (type.equals("upcoming") && eventDate.after(currentDate)) {
                long diff = eventDate.getTime() - currentDate.getTime();
                long daysDiff = diff / (1000 * 60 * 60 * 24);
                if (daysDiff > 5) { // After 5 days
                    filteredEvents.add(event);
                }
            }
        }

        // Update the adapter with the filtered list
        eventAdapter.updateEventList(filteredEvents);
    }
}
