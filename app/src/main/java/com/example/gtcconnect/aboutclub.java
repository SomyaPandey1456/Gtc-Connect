package com.example.gtcconnect;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

public class aboutclub extends Fragment {

    private ImageView clubLogo, headPhoto1, headPhoto2;
    private TextView clubName, clubDescription, headName1, headName2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aboutclub, container, false);

        clubDescription = view.findViewById(R.id.club_info);
        headPhoto1 = view.findViewById(R.id.head_photo1);
        headPhoto2 = view.findViewById(R.id.head_photo2);
        headName1 = view.findViewById(R.id.headname1);
        headName2 = view.findViewById(R.id.headname2);

        // Get clubId from arguments
        String clubId = getArguments() != null ? getArguments().getString("clubId") : null;

        if (clubId != null) {
            fetchClubDetails(clubId);
        } else {
            Toast.makeText(getContext(), "Club ID is missing!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void fetchClubDetails(String clubId) {
        // API URL for fetching club details
        String url = "http://192.168.68.71:3000/club/details/" + clubId;
        Log.d("ClubDetailsActivity", "clubId: " + clubId);
        Log.d("ClubDetailsActivity", "Request URL: " + url);

        // Initialize Volley RequestQueue
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        // Create a GET request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse and display the data
                        try {
                            String description = response.optString("club_desc", "N/A");
                            String head1Name = response.optString("head1_name", "N/A");
                            String head1ImageUrl = response.optString("head1_image", "");
                            String head2Name = response.optString("head2_name", "N/A");
                            String head2ImageUrl = response.optString("head2_image", "");

                            clubDescription.setText(description);

                            Glide.with(aboutclub.this)
                                    .load(head1ImageUrl)
                                    .into(headPhoto1);

                            Glide.with(aboutclub.this)
                                    .load(head2ImageUrl)
                                    .into(headPhoto2);

                            headName1.setText(head1Name);
                            headName2.setText(head2Name);
                        } catch (Exception e) {
                            Log.d("error","Error : " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error","Error : " + error);

                    }
                });

        // Add the request to the RequestQueue
        queue.add(jsonObjectRequest);
    }
}
