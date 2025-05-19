package com.example.gtcconnect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddDivisionActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText divName, gsName, csName, jsName, ciName, aciName, atciName, tresName;
    private Uri gsImageUri, csImageUri, jsImageUri, ciImageUri, aciImageUri, atciImageUri, tresImageUri;
    private Button uploadGSImage, uploadCSImage, uploadJSImage, uploadCIImage, uploadACIImage, uploadATCIImage, uploadTresImage, submitDivision;

    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_division);

        // Initialize views
        divName = findViewById(R.id.divisionName);
        gsName = findViewById(R.id.GeneralSecretaryName);
        csName = findViewById(R.id.CheifSecretaryName);
        jsName = findViewById(R.id.JointSecretaryName);
        ciName = findViewById(R.id.ClubInchargeName);
        aciName = findViewById(R.id.AssociateClubInchargeName);
        atciName = findViewById(R.id.AssistantClubInchargeName);
        tresName = findViewById(R.id.TreasurerName);

        uploadGSImage = findViewById(R.id.uploadGeneralSecretaryPhoto);
        uploadCSImage = findViewById(R.id.uploadCheifSecretaryPhoto);
        uploadJSImage = findViewById(R.id.uploadJointSecretaryPhoto);
        uploadCIImage = findViewById(R.id.uploadClubInchargePhoto);
        uploadACIImage = findViewById(R.id.uploadAssociateClubInchargePhoto);
        uploadATCIImage = findViewById(R.id.uploadAssistantClubInchargePhoto);
        uploadTresImage = findViewById(R.id.uploadTreasurerPhoto);
        submitDivision = findViewById(R.id.adddivision);

        // Retrofit initialization
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.68.71:3000/") // Replace with your backend URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        // Set up button click listeners
        uploadGSImage.setOnClickListener(v -> openImagePicker(1));
        uploadCSImage.setOnClickListener(v -> openImagePicker(2));
        uploadJSImage.setOnClickListener(v -> openImagePicker(3));
        uploadCIImage.setOnClickListener(v -> openImagePicker(4));
        uploadACIImage.setOnClickListener(v -> openImagePicker(5));
        uploadATCIImage.setOnClickListener(v -> openImagePicker(6));
        uploadTresImage.setOnClickListener(v -> openImagePicker(7));

        submitDivision.setOnClickListener(v -> submitDivisionData());
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
                    gsImageUri = selectedImage;
                    break;
                case 2:
                    csImageUri = selectedImage;
                    break;
                case 3:
                    jsImageUri = selectedImage;
                    break;
                case 4:
                    ciImageUri = selectedImage;
                    break;
                case 5:
                    aciImageUri = selectedImage;
                    break;
                case 6:
                    atciImageUri = selectedImage;
                    break;
                case 7:
                    tresImageUri = selectedImage;
                    break;
            }
        }
    }

    private void submitDivisionData() {
        // Validate required fields
        if (divName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Division name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate images
        if (gsImageUri == null) {
            Toast.makeText(this, "General Secretary image is required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare text fields
        RequestBody divNameBody = createRequestBodyIfNotEmpty(divName.getText().toString());
        RequestBody gsNameBody = createRequestBodyIfNotEmpty(gsName.getText().toString());
        RequestBody csNameBody = createRequestBodyIfNotEmpty(csName.getText().toString());
        RequestBody jsNameBody = createRequestBodyIfNotEmpty(jsName.getText().toString());
        RequestBody ciNameBody = createRequestBodyIfNotEmpty(ciName.getText().toString());
        RequestBody aciNameBody = createRequestBodyIfNotEmpty(aciName.getText().toString());
        RequestBody atciNameBody = createRequestBodyIfNotEmpty(atciName.getText().toString());
        RequestBody tresNameBody = createRequestBodyIfNotEmpty(tresName.getText().toString());

        // Prepare images
        MultipartBody.Part gsImagePart = createImagePartIfNotEmpty("gs_image", gsImageUri);
        MultipartBody.Part csImagePart = createImagePartIfNotEmpty("cs_image", csImageUri);
        MultipartBody.Part jsImagePart = createImagePartIfNotEmpty("js_image", jsImageUri);
        MultipartBody.Part ciImagePart = createImagePartIfNotEmpty("ci_image", ciImageUri);
        MultipartBody.Part aciImagePart = createImagePartIfNotEmpty("aci_image", aciImageUri);
        MultipartBody.Part atciImagePart = createImagePartIfNotEmpty("atci_image", atciImageUri);
        MultipartBody.Part tresImagePart = createImagePartIfNotEmpty("tres_image", tresImageUri);

        // Call API
        Call<ResponseBody> call = retrofitInterface.createDivision(
                gsImagePart, csImagePart, jsImagePart, ciImagePart, aciImagePart, atciImagePart, tresImagePart,
                divNameBody, gsNameBody, csNameBody, jsNameBody, ciNameBody, aciNameBody, atciNameBody, tresNameBody
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddDivisionActivity.this, "Division added successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddDivisionActivity.this, "Failed to add division: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddDivisionActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
}
