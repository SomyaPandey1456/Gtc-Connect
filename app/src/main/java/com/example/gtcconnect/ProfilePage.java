package com.example.gtcconnect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilePage extends AppCompatActivity {

    private TextView heading, name, year, admissionNumber, enrollmentNumber, course, phoneNumber, email;
    private Button logoutButton, admindashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Initialize views
        heading = findViewById(R.id.heading);
        name = findViewById(R.id.name);
        year = findViewById(R.id.year);
        admissionNumber = findViewById(R.id.admissionNumber);
        enrollmentNumber = findViewById(R.id.enrollmentNumber);
        course = findViewById(R.id.course);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email);
        logoutButton = findViewById(R.id.logout);
        admindashboard = findViewById(R.id.admindashboard);

        // Set text dynamically (optional)
        heading.setText("My Profile");
        name.setText("Anirban Sarkar");
        year.setText("2021-2025");
        admissionNumber.setText("23CSE1010306");
        enrollmentNumber.setText("23131011958");
        course.setText("BTech, CSE");
        phoneNumber.setText("8368194042");
        email.setText("riotstriker@gmail.com");

        // Handle logout button click
        logoutButton.setOnClickListener(v -> {
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfilePage.this, LoginActivity.class); // Replace with your login activity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        admindashboard.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePage.this, AdminDashboardActivity.class);
            startActivity(intent);
        });
    }
}
