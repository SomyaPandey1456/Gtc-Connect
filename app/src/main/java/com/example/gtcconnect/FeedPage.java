package com.example.gtcconnect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FeedPage extends BaseActivity {

    // Declare views
    private ImageButton imageButton, imageView2;
    private ImageView imageView, imageView3, imageView5, imageView6, imageView7, imageView8, imageView9, imageView11, imageView12, imageView4;
    private TextView textView, textView3, textView4, textView5, textView6, textView7, textView8, textView9, textView10, textView2, textView11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedpage); // Replace with your actual XML file name (without .xml)

        // Initialize views
        imageButton = findViewById(R.id.imageButton);
        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.clublogo);
        imageView3 = findViewById(R.id.imageView3);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.imageView7);
        imageView8 = findViewById(R.id.imageView8);
        imageView9 = findViewById(R.id.imageView9);
        imageView11 = findViewById(R.id.imageView11);
        imageView12 = findViewById(R.id.imageView12);
        imageView4 = findViewById(R.id.imageView4);

        textView = findViewById(R.id.textView);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        textView7 = findViewById(R.id.textView7);
        textView8 = findViewById(R.id.textView8);
        textView9 = findViewById(R.id.textView9);
        textView10 = findViewById(R.id.textView10);
        textView2 = findViewById(R.id.club_info);
        textView11 = findViewById(R.id.textView11);

        // Add click listener for imageView2 to navigate to ProfilePage
        imageView2.setOnClickListener(v -> {
            Intent intent = new Intent(FeedPage.this, ProfilePage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(FeedPage.this, Core_team.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // Initialize Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Highlight the Home menu item
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Set up navigation logic
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true; // Already on Home
            } else if (id == R.id.nav_discover) {
                startActivity(new Intent(FeedPage.this, Event_list.class));
                return true;
            } else if (id == R.id.nav_clubs) {
                startActivity(new Intent(FeedPage.this, DivisionListActivity.class));
                return true;
            }
            return false;
        });
    }
}
