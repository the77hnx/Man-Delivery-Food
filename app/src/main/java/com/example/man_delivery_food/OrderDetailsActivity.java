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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.man_delivery_food.Adapter.ItemDetailsAdpter;
import com.example.man_delivery_food.Model.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    private static final int REQUEST_CALL_PERMISSION = 1;
    private String restaurantPhoneNumber = "0123456789";  // Replace with actual phone number
    private String customerPhoneNumber = "0123456789";    // Replace with actual phone number
    private String restaurantAddress = "حي فاتح نوفمبر لبامة البياضة"; // Replace with actual address
    private String customerAddress = "حي فاتح نوفمبر لبامة البياضة";   // Replace with actual address

    private RecyclerView recyclerView;
    private ItemDetailsAdpter adapter;
    private List<FoodItem> itemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Button callCusBtn = findViewById(R.id.call_user_btn);
        Button directionCusBtn = findViewById(R.id.direction_user_btn);
        Button DeliveryDone = findViewById(R.id.delivery_order_done);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView); // Your RecyclerView ID
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize item list and adapter
        itemList = new ArrayList<>();
        adapter = new ItemDetailsAdpter(this, itemList);
        recyclerView.setAdapter(adapter);

        // Populate the item list
        populateItems();

        DeliveryDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailsActivity.this, OrderInDeliveryActivity.class);
                startActivity(intent);
            }
        });



        callCusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall(customerPhoneNumber);
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

    private void populateItems() {
        itemList.add(new FoodItem("Item 1", 10000.00,10, R.drawable.pizza));
        itemList.add(new FoodItem("Item 2", 100.00,10, R.drawable.pizza));
        itemList.add(new FoodItem("Item 3", 10.00,10, R.drawable.pizza));
        // Add more items as needed

        // Notify adapter of data change
        adapter.notifyDataSetChanged();
    }
}
