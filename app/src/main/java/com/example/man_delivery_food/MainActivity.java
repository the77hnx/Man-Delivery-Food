package com.example.man_delivery_food;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Your main activity layout file

        // Find buttons by ID
        Button acceptOrderButton = findViewById(R.id.detailed_order_btn);
        Button cancelOrderButton = findViewById(R.id.direction_order_btn);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_ma);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home); // Change this based on the activity

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    // Navigate to ShopsActivity
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_following) {
                    // Show toast indicating following action
                    startActivity(new Intent(MainActivity.this, OrderInDeliveryActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_basket) {
                    // Navigate to OrderSummaryActivity
                    startActivity(new Intent(MainActivity.this, OrderHistoryActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    // Navigate to ProfileActivity
                    startActivity(new Intent(MainActivity.this, EditProfileActivity.class));
                    return true;
                }
                return false;
            }
        });

        // Set click listener for accept order button
        acceptOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start OrderDetailsActivity
                Intent intent = new Intent(MainActivity.this, OrderDetailsActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for cancel order button
        cancelOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start OrdersInDeliveryActivity
                Intent intent = new Intent(MainActivity.this, OrderInDeliveryActivity.class);
                startActivity(intent);
            }
        });
    }
}
