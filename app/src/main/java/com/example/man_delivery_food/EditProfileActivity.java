package com.example.man_delivery_food;
import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.man_delivery_food.DBHelper.DBHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;

public class EditProfileActivity extends AppCompatActivity {

    private TextView displayTextView, descriptionTextView, addressTextView, gpsTextView, telrestv, passwordrestv, repasswordrestv;
    private EditText etNumber, etName, addressEditText, etDescription, etpasswordedit, etrepasswordedit;
    private Button editBtn1, editBtn2, editBtn3,editBtn4, editBtn5, editBtn6, btnPhoneLogin;
    private boolean isEditingName = false, isEditingDescription = false, isEditingAddress = false, isEditingpassword = false, isEditingrepassword = false, isEditingtel = false;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private String currentLocation;
    private OkHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize views
        displayTextView = findViewById(R.id.displayTextView);
        descriptionTextView = findViewById(R.id.descriptiontv);
        addressTextView = findViewById(R.id.addresstvwritten);
        telrestv = findViewById(R.id.telrestv);
        passwordrestv = findViewById(R.id.etpasswordedittv);
        repasswordrestv = findViewById(R.id.repasswordrestv);
        etNumber = findViewById(R.id.etNumber);
        etName = findViewById(R.id.etNameeditpage);
        addressEditText = findViewById(R.id.addressEditText);
        etDescription = findViewById(R.id.etDescription);
        etpasswordedit = findViewById(R.id.etpasswordedit);
        etrepasswordedit = findViewById(R.id.etrepasswordedit);
        editBtn1 = findViewById(R.id.editbtn1);
        editBtn2 = findViewById(R.id.editbtn2);
        editBtn3 = findViewById(R.id.editbtn3);
        editBtn4 = findViewById(R.id.editbtn4);
        editBtn5 = findViewById(R.id.editbtn5);
        editBtn6 = findViewById(R.id.editbtn6);
        btnPhoneLogin = findViewById(R.id.saveeditbtn);
        gpsTextView = findViewById(R.id.editGPStext);
        client = new OkHttpClient();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_ep);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile); // Change this based on the activity

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    // Navigate to ShopsActivity
                    startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                    return true;
                }else if (itemId == R.id.navigation_basket) {
                    // Navigate to OrderSummaryActivity
                    startActivity(new Intent(EditProfileActivity.this, OrderInDeliveryActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    // Navigate to ProfileActivity
                    startActivity(new Intent(EditProfileActivity.this, EditProfileActivity.class));
                    return true;
                }
                return false;
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Set click listeners for the buttons
        editBtn1.setOnClickListener(v -> {
            toggleEditText(etNumber, displayTextView, editBtn1, isEditingName);
            isEditingName = !isEditingName;
        });
        editBtn2.setOnClickListener(v -> {
            toggleEditText(etName, descriptionTextView, editBtn2, isEditingDescription);
            isEditingDescription = !isEditingDescription;
        });
        editBtn3.setOnClickListener(v -> {
            toggleEditText(addressEditText, addressTextView, editBtn3, isEditingAddress);
            isEditingAddress = !isEditingAddress;
        });
        editBtn4.setOnClickListener(v -> {
            toggleEditText(etDescription, telrestv, editBtn4, isEditingtel);
            isEditingAddress = !isEditingAddress;
        });
        editBtn5.setOnClickListener(v -> {
            toggleEditText(etpasswordedit, passwordrestv, editBtn5, isEditingpassword);
            isEditingAddress = !isEditingAddress;
        });
        editBtn6.setOnClickListener(v -> {
            toggleEditText(etrepasswordedit, repasswordrestv, editBtn6, isEditingrepassword);
            isEditingAddress = !isEditingAddress;
        });

        DBHelper dbHelper = new DBHelper(this);
        String userId = dbHelper.getUserId();
        Log.d("user id = ", userId) ;

        // Set click listener for the save button
        btnPhoneLogin.setOnClickListener(v -> {
            String numberuser = etNumber.getText().toString();
            String nameuser = etName.getText().toString();
            String numbervehicule = addressEditText.getText().toString();
            String namevehicule = etDescription.getText().toString();
            String password = etpasswordedit.getText().toString();
            String rePassword = etrepasswordedit.getText().toString();

            if (TextUtils.isEmpty(numberuser) || TextUtils.isEmpty(nameuser) ||
                    TextUtils.isEmpty(numbervehicule) || TextUtils.isEmpty(namevehicule) ||
                    TextUtils.isEmpty(rePassword) || TextUtils.isEmpty(rePassword)) {
                Toast.makeText(EditProfileActivity.this, "Please enter fields.", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(rePassword)) {
                Toast.makeText(EditProfileActivity.this, "Passwords do not match. Please re-enter.", Toast.LENGTH_SHORT).show();
            } else {
                // Save all edits if passwords match
                saveAllEdits(userId);
                // Navigate to the menu page
                Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        gpsTextView.setOnClickListener(v -> checkLocationSettings());
    }

    private void checkLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, locationSettingsResponse -> requestLocationPermission());

        task.addOnFailureListener(this, e -> {
            if (e instanceof com.google.android.gms.common.api.ResolvableApiException) {
                try {
                    com.google.android.gms.common.api.ResolvableApiException resolvable = (com.google.android.gms.common.api.ResolvableApiException) e;
                    resolvable.startResolutionForResult(EditProfileActivity.this, LOCATION_PERMISSION_REQUEST_CODE);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            } else {
                Snackbar.make(findViewById(R.id.main), "Location settings are not satisfied.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Settings", v -> {
                            // Open settings
                        }).show();
            }
        });
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            try {
                                Geocoder geocoder = new Geocoder(EditProfileActivity.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (addresses != null && !addresses.isEmpty()) {
                                    Address address = addresses.get(0);
                                    currentLocation = address.getAddressLine(0);
                                    gpsTextView.setText(currentLocation);
                                } else {
                                    Toast.makeText(EditProfileActivity.this, "Unable to get address", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(EditProfileActivity.this, "Unable to get address", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Permission denied to access location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied to access location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void toggleEditText(EditText editText, TextView textView, Button button, boolean isEditing) {
        if (isEditing) {
            // Save changes and switch to TextView
            String newText = editText.getText().toString();
            if (!TextUtils.isEmpty(newText)) {
                textView.setText(newText);
            }
            textView.setVisibility(View.VISIBLE);
            editText.setVisibility(View.GONE);
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_edit));
        } else {
            // Switch to EditText
            textView.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_done));
        }
    }

    private void saveAllEdits(String userId) {
        // Prepare data to send
        String livreurName = etName.getText().toString();
        String vehiculeName = etDescription.getText().toString();
        String phoneNumber = etNumber.getText().toString();
        String password = etpasswordedit.getText().toString();
        String numberVehicule = addressEditText.getText().toString();
        String coordinates = gpsTextView.getText().toString(); // Assuming current GPS address


        // Log the values before sending the request
        Log.d("EditProfileActivity", "livreur Name: " + livreurName);
        Log.d("EditProfileActivity", "vehicule Name: " + vehiculeName);
        Log.d("EditProfileActivity", "Phone Number: " + phoneNumber);
        Log.d("EditProfileActivity", "Password: " + password); // Be cautious with logging sensitive data
        Log.d("EditProfileActivity", "number Vehicule: " + numberVehicule);
        Log.d("EditProfileActivity", "Coordinates: " + coordinates);

        // Perform network request here
        String url = "https://www.fissadelivery.com/fissa/Man_Delivery_Food/Update_Profile.php"; // Update with your PHP endpoint

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle response
                    Log.d("EditProfileActivity", "Response: " + response); // Log the response
                    Toast.makeText(EditProfileActivity.this, response, Toast.LENGTH_SHORT).show(); // Show server message
                },
                error -> {
                    // Handle error
                    Log.e("EditProfileActivity", "Error updating profile: " + error.getMessage()); // Log the error message
                    Toast.makeText(EditProfileActivity.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Nom_Livreur", livreurName);
                params.put("Nom_Vehicule", vehiculeName);
                params.put("Tel_Livreur", phoneNumber);
                params.put("Password", password);
                params.put("N_Vehicule", numberVehicule);
                params.put("Coordonnes", coordinates);
                params.put("user_id", userId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add request to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }


}
