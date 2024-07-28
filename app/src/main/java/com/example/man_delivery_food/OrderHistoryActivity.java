package com.example.man_delivery_food;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.man_delivery_food.Adapter.OrderAdapter;
import com.example.man_delivery_food.Model.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_history);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_oh);
        bottomNavigationView.setSelectedItemId(R.id.navigation_basket); // Change this based on the activity

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    // Navigate to ShopsActivity
                    startActivity(new Intent(OrderHistoryActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_following) {
                    // Show toast indicating following action
                    startActivity(new Intent(OrderHistoryActivity.this, OrderInDeliveryActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_basket) {
                    // Navigate to OrderSummaryActivity
                    startActivity(new Intent(OrderHistoryActivity.this, OrderHistoryActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    // Navigate to ProfileActivity
                    startActivity(new Intent(OrderHistoryActivity.this, EditProfileActivity.class));
                    return true;
                }
                return false;
            }
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize data
        orderList = getSampleOrders();

        // Set adapter
        orderAdapter = new OrderAdapter(this, orderList);
        recyclerView.setAdapter(orderAdapter);

        // Handle system insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private List<Order> getSampleOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("1324", "2", "30/11/2024", "11:05"));
        orders.add(new Order("5678", "3", "01/12/2024", "09:30"));
        orders.add(new Order("91011", "1", "02/12/2024", "15:45"));
        orders.add(new Order("1324", "2", "30/11/2024", "11:05"));
        orders.add(new Order("5678", "3", "01/12/2024", "09:30"));
        orders.add(new Order("91011", "1", "02/12/2024", "15:45"));
        orders.add(new Order("1324", "2", "30/11/2024", "11:05"));
        orders.add(new Order("5678", "3", "01/12/2024", "09:30"));
        orders.add(new Order("91011", "1", "02/12/2024", "15:45"));
        orders.add(new Order("1324", "2", "30/11/2024", "11:05"));
        orders.add(new Order("5678", "3", "01/12/2024", "09:30"));
        orders.add(new Order("91011", "1", "02/12/2024", "15:45"));
        orders.add(new Order("1324", "2", "30/11/2024", "11:05"));
        orders.add(new Order("5678", "3", "01/12/2024", "09:30"));
        orders.add(new Order("91011", "1", "02/12/2024", "15:45"));
        orders.add(new Order("1324", "2", "30/11/2024", "11:05"));
        orders.add(new Order("5678", "3", "01/12/2024", "09:30"));
        orders.add(new Order("91011", "1", "02/12/2024", "15:45"));
        orders.add(new Order("1324", "2", "30/11/2024", "11:05"));
        orders.add(new Order("5678", "3", "01/12/2024", "09:30"));
        orders.add(new Order("91011", "1", "02/12/2024", "15:45"));
        orders.add(new Order("1324", "2", "30/11/2024", "11:05"));
        orders.add(new Order("5678", "3", "01/12/2024", "09:30"));
        orders.add(new Order("91011", "1", "02/12/2024", "15:45"));

        // Add more orders if needed
        return orders;
    }
}
