package com.example.gtcconnect;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class addEvent extends AppCompatActivity {

    private EditText etEventName, etEventVenue, etChiefGuest, etEventDescription, etEventTime;
    private TextView tvEventDate, tvClubName;
    private ImageView ivClubLogo, ivEventPoster;
    private Button btnUploadPoster, btnAddEvent, btnCancel;
    private Spinner spinnerEventType;

    private Uri posterUri;
    private Calendar eventDateCalendar = Calendar.getInstance();
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevent_details);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.68.71:3000/") // Replace with your backend URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        String clubId = getIntent().getStringExtra("clubId");
        if (clubId != null) {
            fetchClubDetails(clubId);
        } else {
            Toast.makeText(this, "Club ID not found!", Toast.LENGTH_SHORT).show();
        }

        etEventName = findViewById(R.id.editText_event_name);
        etEventVenue = findViewById(R.id.editText_event_venue);
        etChiefGuest = findViewById(R.id.editText_chief_guest);
        etEventDescription = findViewById(R.id.editText_event_description);
        etEventTime = findViewById(R.id.editText_event_timing);
        tvEventDate = findViewById(R.id.editText_event_date);
        tvClubName = findViewById(R.id.clubname);
        ivClubLogo = findViewById(R.id.clublogo);
        ivEventPoster = findViewById(R.id.eventposter);
        btnUploadPoster = findViewById(R.id.eventbanner);
        btnAddEvent = findViewById(R.id.button_add_event);
        btnCancel = findViewById(R.id.button_cancel);
        spinnerEventType = findViewById(R.id.spinner_event_type);

        tvEventDate.setOnClickListener(v -> new DatePickerDialog(this, (view, year, month, day) -> {
            eventDateCalendar.set(Calendar.YEAR, year);
            eventDateCalendar.set(Calendar.MONTH, month);
            eventDateCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateDateLabel();
        }, eventDateCalendar.get(Calendar.YEAR), eventDateCalendar.get(Calendar.MONTH), eventDateCalendar.get(Calendar.DAY_OF_MONTH)).show());

        btnUploadPoster.setOnClickListener(v -> pickImage());
        btnAddEvent.setOnClickListener(v -> addEvent(clubId));
        btnCancel.setOnClickListener(v -> finish());
    }

    private void updateDateLabel() {
        String dateFormat = "yyyy-MM-dd"; // Ensure the format matches the backend requirements
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        String formattedDate = sdf.format(eventDateCalendar.getTime());
        tvEventDate.setText(formattedDate);
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            posterUri = data.getData();
            ivEventPoster.setImageURI(posterUri);
        }
    }

    private void addEvent(String clubId) {
        String eventName = etEventName.getText().toString().trim();
        String eventVenue = etEventVenue.getText().toString().trim();
        String chiefGuest = etChiefGuest.getText().toString().trim();
        String eventDescription = etEventDescription.getText().toString().trim();
        String eventDate = tvEventDate.getText().toString().trim();
        String eventTime = etEventTime.getText().toString().trim();
        String selectedEventType = spinnerEventType.getSelectedItem().toString();

        if (TextUtils.isEmpty(eventName) || TextUtils.isEmpty(eventVenue) || TextUtils.isEmpty(eventDate) || TextUtils.isEmpty(eventTime)) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        MultipartBody.Part posterPart = null;
        if (posterUri != null) {
            File posterFile = FileUtils.getFileFromUri(this, posterUri);
            if (posterFile != null) {
                RequestBody posterRequestBody = RequestBody.create(MediaType.parse("image/*"), posterFile);
                posterPart = MultipartBody.Part.createFormData("eventPoster", posterFile.getName(), posterRequestBody);
            } else {
                Toast.makeText(this, "Invalid poster file", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        RequestBody eventNameBody = RequestBody.create(MediaType.parse("text/plain"), eventName);
        RequestBody eventVenueBody = RequestBody.create(MediaType.parse("text/plain"), eventVenue);
        RequestBody chiefGuestBody = RequestBody.create(MediaType.parse("text/plain"), chiefGuest);
        RequestBody eventDescriptionBody = RequestBody.create(MediaType.parse("text/plain"), eventDescription);
        RequestBody eventDateBody = RequestBody.create(MediaType.parse("text/plain"), eventDate);
        RequestBody eventTimeBody = RequestBody.create(MediaType.parse("text/plain"), eventTime);
        RequestBody eventTypeBody = RequestBody.create(MediaType.parse("text/plain"), selectedEventType);
        RequestBody clubIdBody = RequestBody.create(MediaType.parse("text/plain"), clubId);

        Call<ResponseBody> call = retrofitInterface.createEvent(posterPart, eventNameBody, eventVenueBody, eventDateBody, eventTimeBody, eventDescriptionBody, chiefGuestBody, clubIdBody, eventTypeBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(addEvent.this, "Event added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(addEvent.this, "Failed to add event", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(addEvent.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchClubDetails(String clubId) {
        String url = "http://192.168.1.4:3000/club/details/" + clubId;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        String name = response.optString("club_name", "N/A");
                        String logoUrl = response.optString("club_image", "");

                        Glide.with(addEvent.this)
                                .load(logoUrl)
                                .into(ivClubLogo);

                        tvClubName.setText(name);

                    } catch (Exception e) {
                        Toast.makeText(addEvent.this, "Error parsing club details", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(addEvent.this, "Error fetching club details", Toast.LENGTH_SHORT).show()
        );

        queue.add(jsonObjectRequest);
    }
}
