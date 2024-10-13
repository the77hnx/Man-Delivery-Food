package com.example.man_delivery_food;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.man_delivery_food.Model.Order;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderInDeliveryActivity extends AppCompatActivity {

    private LinearLayout deliveryDoneLayout;
    private EditText otpInput1, otpInput2;
    private boolean isOrderCancelled = false; // Flag to track button state

    private Button Receive_Done_orderBTN;
    private Button DetailedOrderBtn;
    private Button Return_Main;

    private List<Order> order;
    private TextView basket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_in_delivery);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_od);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    // Navigate to ShopsActivity
                    startActivity(new Intent(OrderInDeliveryActivity.this, MainActivity.class));
                    return true;
                }else if (itemId == R.id.navigation_basket) {
                    // Navigate to OrderSummaryActivity
                    startActivity(new Intent(OrderInDeliveryActivity.this, OrderInDeliveryActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    // Navigate to ProfileActivity
                    startActivity(new Intent(OrderInDeliveryActivity.this, EditProfileActivity.class));
                    return true;
                }
                return false;
            }
        });

        // Initialize Views
        basket = findViewById(R.id.basket);
        deliveryDoneLayout = findViewById(R.id.delivery_done);
        Return_Main = findViewById(R.id.submit_otp);
        Receive_Done_orderBTN = findViewById(R.id.Receive_Done_order);
        DetailedOrderBtn = findViewById(R.id.detailed_order_btn_od);

        // Load the Map Fragment
        loadMapFragment();

        DetailedOrderBtn.setOnClickListener(v -> {
            // Hide delivery_done layout and show otp_section layout
            if (order != null && !order.isEmpty()) {
                String orderId = order.get(0).getOrderId(); // Get the ID of the first order
                Intent intent = new Intent(OrderInDeliveryActivity.this, OrderDetailsActivity.class);
                intent.putExtra("Id_Demandes", orderId);
                startActivity(intent);
            } else {
                Toast.makeText(OrderInDeliveryActivity.this, "No orders available", Toast.LENGTH_SHORT).show();
            }
        });

        String orderId = getIntent().getStringExtra("Id_Demandes");

        if (orderId != null) {
            try {
                int parsedOrderId = Integer.parseInt(orderId); // Parse orderId to ensure it's a valid integer
                basket.setText(String.valueOf(parsedOrderId)); // Display the parsed orderId
            } catch (NumberFormatException e) {
                Log.e("OrderInDeliveryActivity", "Invalid order ID format", e);
                Toast.makeText(this, "Invalid order ID", Toast.LENGTH_SHORT).show();
            }
        }

        // Set onClickListener for Receive_Done_orderBTN
        Receive_Done_orderBTN.setOnClickListener(v -> {
            if (!isOrderCancelled) {
                // First click: Update orderId to 4
                updateOrderStatus(Integer.parseInt(orderId), 4); // You might want to set statusId according to your status logic
                Receive_Done_orderBTN.setText("تم التوصيل");
                isOrderCancelled = true;
            } else {
                // Second click: Update orderId to 6
                updateOrderStatus(Integer.parseInt(orderId), 6); // Update statusId as needed
                deliveryDoneLayout.setVisibility(View.GONE);
                Return_Main.setVisibility(View.VISIBLE);
                Toast.makeText(OrderInDeliveryActivity.this, "نشكرك على اخلاصك في العمل", Toast.LENGTH_SHORT).show();
                isOrderCancelled = false; // Reset the flag if needed
            }
        });

        Return_Main.setOnClickListener(v -> {
                Intent intent = new Intent(OrderInDeliveryActivity.this, MainActivity.class);
                startActivity(intent);
        });
    }

    private void updateOrderStatus(int orderId, int statusId) {
        OkHttpClient client = new OkHttpClient();

        // Convert orderId and statusId to English numerals
        String orderIdEnglish = convertArabicToEnglish(String.valueOf(orderId));
        String statusIdEnglish = convertArabicToEnglish(String.valueOf(statusId));

        // Create JSON string with English numerals
        String json = String.format("{\"Id_Demandes\": %s, \"Id_Statut_Commande\": %s}", orderIdEnglish, statusIdEnglish);

        Log.d("Request JSON", json); // Log the JSON payload

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url("http://192.168.1.33/fissa/Man_Delivery_Food/Update_Status_cmd.php")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string(); // Read response body
                Log.d("Response Data", responseData); // Log the response for debugging

                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(OrderInDeliveryActivity.this, "Order status updated successfully", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(OrderInDeliveryActivity.this, "Error: " + responseData, Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(OrderInDeliveryActivity.this, "Failed to update order status", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    // Method to convert Arabic numerals to English numerals
    private String convertArabicToEnglish(String number) {
        return number.replace('٠', '0')
                .replace('١', '1')
                .replace('٢', '2')
                .replace('٣', '3')
                .replace('٤', '4')
                .replace('٥', '5')
                .replace('٦', '6')
                .replace('٧', '7')
                .replace('٨', '8')
                .replace('٩', '9');
    }


    private void loadMapFragment() {
        SupportMapFragment mapFragment = new SupportMapFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.map_container, mapFragment);
        fragmentTransaction.commit();
    }
}
