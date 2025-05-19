package com.example.gtcconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavbar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navbar); // Ensure this matches your layout name

        // Initialize Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set item selected listener using lambda
        bottomNavigationView.setOnItemSelectedListener(item -> {
            // Use if-else blocks instead of switch for non-final resource IDs
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Navigate to Home
                startActivity(new Intent(BottomNavbar.this, FeedPage.class));
                return true;
            } else if (id == R.id.nav_discover) {
                // Navigate to Discover
                startActivity(new Intent(BottomNavbar.this, Event_list.class));
                return true;
            } else if (id == R.id.nav_clubs) {
                // Navigate to Clubs
                startActivity(new Intent(BottomNavbar.this, DivisionListActivity.class));
                return true;
            }
            return false;
        });
    }
}
