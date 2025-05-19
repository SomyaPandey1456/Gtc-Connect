package com.example.gtcconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navbar);  // Base layout with BottomNavigationView

        // Initialize Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set item selected listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Navigate to Home
                startActivity(new Intent(BaseActivity.this, FeedPage.class));
                return true;
            } else if (id == R.id.nav_discover) {
                // Navigate to Discover
                startActivity(new Intent(BaseActivity.this, Event_list.class));
                return true;
            } else if (id == R.id.nav_clubs) {
                // Navigate to Clubs
                startActivity(new Intent(BaseActivity.this, DivisionListActivity.class));
                return true;
            }
            return false;
        });
    }
}
