package com.example.gtcconnect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gtcconnect.R;

public class AdminDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        Button btnAddDivision = findViewById(R.id.addDivisionButton);
        Button btnEditDivision = findViewById(R.id.editDivisionButton);
        Button btnAddClub = findViewById(R.id.addClubButton);
        Button btnEditClub = findViewById(R.id.editClubButton);

        btnAddDivision.setOnClickListener(v -> startActivity(new Intent(this, AddDivisionActivity.class)));
        btnAddClub.setOnClickListener(v -> startActivity(new Intent(this, AddClubActivity.class)));

    }
}
