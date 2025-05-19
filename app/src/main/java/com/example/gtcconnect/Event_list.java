package com.example.gtcconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Event_list extends AppCompatActivity {
    private RecyclerView eventListView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private static final String TAG = "Event_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        eventListView = findViewById(R.id.eventListView);
        TextView viewAll = findViewById(R.id.viewall);

        // Set OnClickListener for "View All" button
        viewAll.setOnClickListener(view -> {
            startActivity(new Intent(Event_list.this, AllEventsActivity.class));
        });

        // Initialize the event list
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(this, eventList, event -> {
            // Handle click on an event
            Intent intent = new Intent(Event_list.this, EventDetailsActivity.class);
            intent.putExtra("event_id", event.getId()); // Pass the selected event ID
            startActivity(intent);
        });
        eventListView.setLayoutManager(new LinearLayoutManager(this));
        eventListView.setAdapter(eventAdapter);

        // Fetch and display hot events
        fetchHotEvents();

        // Initialize Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Highlight the Discover menu item
        bottomNavigationView.setSelectedItemId(R.id.nav_discover);

        // Set up navigation logic
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(Event_list.this, FeedPage.class));
                return true;
            } else if (id == R.id.nav_discover) {
                return true;
            } else if (id == R.id.nav_clubs) {
                startActivity(new Intent(Event_list.this, DivisionListActivity.class));
                return true;
            }
            return false;
        });
    }

    /**
     * Fetch hot events (events happening within the next 2 days) from the backend.
     */
    private void fetchHotEvents() {
        String url = "http://192.168.68.71:3000/events/getall"; // Replace with your API endpoint
        Log.d(TAG, "Request URL: " + url);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            eventList.clear(); // Clear existing list
                            JSONArray eventsArray = response.getJSONArray("events");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                            for (int i = 0; i < eventsArray.length(); i++) {
                                JSONObject eventObject = eventsArray.getJSONObject(i);

                                String id = eventObject.optString("id", "");
                                String title = eventObject.optString("eventName", "N/A");
                                String eventIconUrl = eventObject.optString("eventPoster", "");
                                String venue = eventObject.optString("eventVenue", "N/A");
                                String description = eventObject.optString("eventDescription", "N/A");
                                String type = eventObject.optString("eventType", "N/A");
                                String dateStr = eventObject.optString("eventDate", "N/A");

                                // Parse date string into Date object
                                Date eventDate = null;
                                try {
                                    eventDate = sdf.parse(dateStr);
                                } catch (ParseException e) {
                                    Log.e(TAG, "Error parsing date: " + e.getMessage());
                                }

                                // Check if the event is within the next 2 days
                                if (isHotEvent(eventDate)) {
                                    // Add the event to the list
                                    eventList.add(new Event(id, title, eventIconUrl, venue, description, type, eventDate));
                                }
                            }

                            // Notify the adapter of data changes
                            eventAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error fetching hot events: " + error.getMessage());
                    }
                });

        queue.add(jsonObjectRequest);
    }

    private boolean isHotEvent(Date eventDate) {
        if (eventDate == null) return false;

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Calculate date 2 days from now
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date twoDaysFromNow = calendar.getTime();

        // Check if event date is within the range
        return eventDate.after(currentDate) && eventDate.before(twoDaysFromNow);
    }
}
