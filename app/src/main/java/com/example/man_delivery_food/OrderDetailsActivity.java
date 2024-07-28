package com.example.man_delivery_food;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class OrderDetailsActivity extends AppCompatActivity {

    private static final int REQUEST_CALL_PERMISSION = 1;
    private String restaurantPhoneNumber = "0123456789";  // Replace with actual phone number
    private String customerPhoneNumber = "0123456789";    // Replace with actual phone number
    private String restaurantAddress = "حي فاتح نوفمبر لبامة البياضة"; // Replace with actual address
    private String customerAddress = "حي فاتح نوفمبر لبامة البياضة";   // Replace with actual address

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Button callResBtn = findViewById(R.id.call_res_btn);
        Button directionResBtn = findViewById(R.id.direction_res_btn);
        Button callCusBtn = findViewById(R.id.call_user_btn);
        Button directionCusBtn = findViewById(R.id.direction_user_btn);
        Button DeliveryDone = findViewById(R.id.delivery_order_done);

        DeliveryDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailsActivity.this, OrderInDeliveryActivity.class);
                startActivity(intent);
            }
        });

        callResBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall(restaurantPhoneNumber);
            }
        });

        callCusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall(customerPhoneNumber);
            }
        });

        directionResBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapForDirections(36.7201600, 3.2167000, restaurantAddress);  // Use actual coordinates for the restaurant
            }
        });

        directionCusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapForDirections(36.7525000, 3.0420000, customerAddress);  // Use actual coordinates for the customer
            }
        });
    }

    private void makePhoneCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            startPhoneCall(phoneNumber);
        }
    }

    private void startPhoneCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            startActivity(callIntent);
        } catch (SecurityException e) {
            Toast.makeText(this, "Permission denied to make a call", Toast.LENGTH_SHORT).show();
        }
    }

    private void openMapForDirections(double latitude, double longitude, String address) {
        // Create a URI to pass to the Google Maps app with the directions
        String uri = String.format("google.navigation:q=%f,%f(%s)", latitude, longitude, Uri.encode(address));
        Uri gmmIntentUri = Uri.parse(uri);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Maps application is not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Handle phone call after permission is granted
                Toast.makeText(this, "Permission granted to make a call", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied to make a call", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
