package com.example.gtcconnect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddClubActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText clubName, head1Name, head2Name, clubDesc;
    private Spinner divNameSpinner;
    private Uri clubImageUri, head1ImageUri, head2ImageUri;
    private Button uploadClubImage, uploadHead1Image, uploadHead2Image, submitClub;

    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_club);

        // Initialize views
        clubName = findViewById(R.id.clubName);
        head1Name = findViewById(R.id.head1Name);
        head2Name = findViewById(R.id.head2Name);
        clubDesc = findViewById(R.id.clubDesc);
        divNameSpinner = findViewById(R.id.divNameSpinner);

        uploadClubImage = findViewById(R.id.uploadClubImage);
        uploadHead1Image = findViewById(R.id.uploadHead1Image);
        uploadHead2Image = findViewById(R.id.uploadHead2Image);
        submitClub = findViewById(R.id.submitClub);

        // Retrofit initialization
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.16.68.71:3000/") // Replace with your backend URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        // Load divisions into dropdown
        loadDivisions();

        // Set up button click listeners
        uploadClubImage.setOnClickListener(v -> openImagePicker(1));
        uploadHead1Image.setOnClickListener(v -> openImagePicker(2));
        uploadHead2Image.setOnClickListener(v -> openImagePicker(3));

        submitClub.setOnClickListener(v -> submitClubData());
    }

    private void openImagePicker(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            switch (requestCode) {
                case 1:
                    clubImageUri = selectedImage;
                    break;
                case 2:
                    head1ImageUri = selectedImage;
                    break;
                case 3:
                    head2ImageUri = selectedImage;
                    break;
            }
        }
    }

    private void submitClubData() {
        // Prepare data for Retrofit
        RequestBody clubNameBody = createRequestBodyIfNotEmpty(clubName.getText().toString());
        RequestBody clubDescBody = createRequestBodyIfNotEmpty(clubDesc.getText().toString());
        RequestBody head1NameBody = createRequestBodyIfNotEmpty(head1Name.getText().toString());
        RequestBody head2NameBody = createRequestBodyIfNotEmpty(head2Name.getText().toString());
        RequestBody divNameBody = createRequestBodyIfNotEmpty(divNameSpinner.getSelectedItem().toString());

        // Create image parts only if the URI is not null
        MultipartBody.Part clubImagePart = createImagePartIfNotEmpty("club_image", clubImageUri);
        MultipartBody.Part head1ImagePart = createImagePartIfNotEmpty("head1_image", head1ImageUri);
        MultipartBody.Part head2ImagePart = createImagePartIfNotEmpty("head2_image", head2ImageUri);

        // Call Retrofit API to create club
        Call<ResponseBody> call = retrofitInterface.createClub(
                clubImagePart, head1ImagePart, head2ImagePart,
                clubNameBody, divNameBody, clubDescBody, head1NameBody, head2NameBody
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddClubActivity.this, "Club added successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddClubActivity.this, "Failed to add club: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddClubActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private RequestBody createRequestBodyIfNotEmpty(String value) {
        return value.isEmpty() ? null : RequestBody.create(MediaType.parse("text/plain"), value);
    }

    private MultipartBody.Part createImagePartIfNotEmpty(String fieldName, Uri imageUri) {
        if (imageUri == null) return null; // Skip if no image is selected
        File imageFile = FileUtils.getFileFromUri(this, imageUri);
        return MultipartBody.Part.createFormData(
                fieldName, imageFile.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile)
        );
    }

    private void loadDivisions() {
        // Fetch divisions list from the backend
        retrofitInterface.getDivisions().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Populate the spinner with divisions
                    List<String> divisions = response.body();
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddClubActivity.this, android.R.layout.simple_spinner_item, divisions);
                    divNameSpinner.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(AddClubActivity.this, "Failed to load divisions: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}