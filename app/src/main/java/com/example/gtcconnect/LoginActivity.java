package com.example.gtcconnect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;

    // Hardcoded test credentials
    private final String TEST_EMAIL = "anirbansarkar@gmail.com";
    private final String TEST_PASSWORD = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.button2);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Check if user credentials match the test credentials
            if (email.equals(TEST_EMAIL) && password.equals(TEST_PASSWORD)) {
                Toast.makeText(LoginActivity.this, "Test Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, FeedPage.class));
            } else {
                // Otherwise, proceed with the normal login flow
                loginUser(email, password);
            }
        });
    }

    private void loginUser(String email, String password) {
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.68.71:3000/users/login") // Change to your server URL
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, FeedPage.class));
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
