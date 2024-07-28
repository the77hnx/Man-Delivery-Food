package com.example.man_delivery_food;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class EditProfileActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final int REQUEST_CAMERA_PERMISSION = 100; // Camera permission request code

    private ImageView profileImageView;
    private Uri imageUri;

    private TextView displayTextView, descriptionTextView;
    private EditText etNumber, etName;
    private Button editBtn1, editBtn2, editProfileImageButton, btnPhoneLogin;
    private boolean isEditingName = false, isEditingDescription = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize views
        profileImageView = findViewById(R.id.profileImageView);
        displayTextView = findViewById(R.id.displayTextView);
        descriptionTextView = findViewById(R.id.numbertv);
        etNumber = findViewById(R.id.etnumberedit);
        etName = findViewById(R.id.etNameedit);
        editProfileImageButton = findViewById(R.id.editProfileImageButton);
        editBtn1 = findViewById(R.id.editbtn1);
        editBtn2 = findViewById(R.id.editbtn2);
        btnPhoneLogin = findViewById(R.id.saveeditbtn);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_ep);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_following) {
                    startActivity(new Intent(EditProfileActivity.this, OrderInDeliveryActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_basket) {
                    startActivity(new Intent(EditProfileActivity.this, OrderHistoryActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    startActivity(new Intent(EditProfileActivity.this, EditProfileActivity.class));
                    return true;
                }
                return false;
            }
        });

        editProfileImageButton.setOnClickListener(v -> showImagePickerDialog(profileImageLauncher));

        // Set click listeners for the buttons
        editBtn1.setOnClickListener(v -> {
            toggleEditText(etNumber, displayTextView, editBtn1, isEditingName);
            isEditingName = !isEditingName;
        });

        editBtn2.setOnClickListener(v -> {
            toggleEditText(etName, descriptionTextView, editBtn2, isEditingDescription);
            isEditingDescription = !isEditingDescription;
        });

        // Set click listener for the save button
        btnPhoneLogin.setOnClickListener(v -> {
            saveAllEdits();
            Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });
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
            editText.setText(textView.getText());
            textView.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_done));
        }
    }

    private void saveAllEdits() {
        if (etNumber.getVisibility() == View.VISIBLE) {
            String newNumber = etNumber.getText().toString();
            if (!TextUtils.isEmpty(newNumber)) {
                displayTextView.setText(newNumber);
            }
            etNumber.setVisibility(View.GONE);
            displayTextView.setVisibility(View.VISIBLE);
        }

        if (etName.getVisibility() == View.VISIBLE) {
            String newName = etName.getText().toString();
            if (!TextUtils.isEmpty(newName)) {
                descriptionTextView.setText(newName);
            }
            etName.setVisibility(View.GONE);
            descriptionTextView.setVisibility(View.VISIBLE);
        }
    }

    private void showImagePickerDialog(ActivityResultLauncher<String> launcher) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_image_picker, null);
        bottomSheetDialog.setContentView(dialogView);

        Button chooseFromGallery = dialogView.findViewById(R.id.btnPickGallery);
        Button capturePhoto = dialogView.findViewById(R.id.btnCaptureCamera);

        chooseFromGallery.setOnClickListener(v -> {
            launcher.launch("image/*");
            bottomSheetDialog.dismiss();
        });

        capturePhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera(launcher);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void openCamera(ActivityResultLauncher<String> launcher) {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No camera app available", Toast.LENGTH_SHORT).show();
        }
    }

    ActivityResultLauncher<String> profileImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        profileImageView.setImageURI(uri);
                    }
                }
            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    profileImageView.setImageURI(imageUri);
                } else {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Camera permission is required to use the camera", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
