package com.example.man_delivery_food;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OrderInDeliveryActivity extends AppCompatActivity {

    private LinearLayout deliveryDoneLayout;
    private LinearLayout otpSection;
    private EditText otpInput1 , otpInput2;
    private boolean isOrderCancelled = false; // Flag to track button state

    private Button cancelOrderButton;
    private Button submitOtpButton;
    private Button DetailedOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_in_delivery);

        // Initialize Views
        deliveryDoneLayout = findViewById(R.id.delivery_done);
        otpSection = findViewById(R.id.otp_section);
        otpInput1 = findViewById(R.id.inputcode1);
        otpInput2 = findViewById(R.id.inputcode2);
        cancelOrderButton = findViewById(R.id.cancel_order);
        submitOtpButton = findViewById(R.id.submit_otp);
        DetailedOrderBtn = findViewById(R.id.detailed_order_btn_od);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_od);
        bottomNavigationView.setSelectedItemId(R.id.navigation_following); // Change this based on the activity

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    // Navigate to ShopsActivity
                    startActivity(new Intent(OrderInDeliveryActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_following) {
                    // Show toast indicating following action
                    startActivity(new Intent(OrderInDeliveryActivity.this, OrderInDeliveryActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_basket) {
                    // Navigate to OrderSummaryActivity
                    startActivity(new Intent(OrderInDeliveryActivity.this, OrderHistoryActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    // Navigate to ProfileActivity
                    startActivity(new Intent(OrderInDeliveryActivity.this, EditProfileActivity.class));
                    return true;
                }
                return false;
            }
        });

        // Load the Map Fragment
        loadMapFragment();

        DetailedOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide delivery_done layout and show otp_section layout

                Intent intent = new Intent(OrderInDeliveryActivity.this , OrderDetailsActivity.class);
                startActivity(intent);

            }
        });


        // Set onClickListener for cancelOrderButton
        cancelOrderButton.setOnClickListener(v -> {
            if (!isOrderCancelled) {
                // Change the text and set flag to true
                cancelOrderButton.setText("تم التوصيل");
                isOrderCancelled = true;
            } else {
                // Hide delivery_done layout and show otp_section layout
                deliveryDoneLayout.setVisibility(View.GONE);
                otpSection.setVisibility(View.VISIBLE);
                isOrderCancelled = false; // Reset the flag if needed
            }
        });


        // Set onClickListener for submitOtpButton
        submitOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate OTP
                validateOtp();
            }
        });
    }

    private void loadMapFragment() {
        SupportMapFragment mapFragment = new SupportMapFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.map_container, mapFragment);
        fragmentTransaction.commit();
    }

    private void validateOtp() {
        String enteredOtp1 = otpInput1.getText().toString().trim();
        String enteredOtp2 = otpInput2.getText().toString().trim();


        // Example OTP for validation
        String correctOtp1 = "1";
        String correctOtp2 = "2";


        if (enteredOtp1.equals(correctOtp1) && enteredOtp2.equals(correctOtp2) ){
            // OTP is correct
            Toast.makeText(OrderInDeliveryActivity.this, "OTP is correct!", Toast.LENGTH_SHORT).show();

            deliveryDoneLayout.setVisibility(View.VISIBLE);
            otpSection.setVisibility(View.GONE);

            Intent intent = new Intent(OrderInDeliveryActivity.this , MainActivity.class);
            startActivity(intent);
            // Perform the necessary action for a successful OTP validation
            // For example, mark the order as completed or notify the user
        } else {
            // OTP is incorrect
            Toast.makeText(OrderInDeliveryActivity.this, "Incorrect OTP. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
