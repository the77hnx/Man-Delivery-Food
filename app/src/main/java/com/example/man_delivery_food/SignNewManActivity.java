package com.example.man_delivery_food;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;

public class SignNewManActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_VEH_ID = 2;
    private static final int REQUEST_IMAGE_CARD_ID = 3;
    private static final int REQUEST_CAMERA_PERMISSION = 100; // Added constant for camera permission

    private EditText etNumber, etName, etDescriptionVeh, etPlacesVeh;
    private Button selectImageVehButton, vehIDButton, cardIDButton, btnPhoneLogin;
    private ImageView selectedImageVeh, selectedVehID, selectedCardID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_new_man);

        initializeViews();

        selectImageVehButton.setOnClickListener(v -> showImagePickerDialog(selectImagevehLauncher));
        vehIDButton.setOnClickListener(v -> showImagePickerDialog(vehIDLauncher));
        cardIDButton.setOnClickListener(v -> showImagePickerDialog(cardIDLauncher));
        btnPhoneLogin.setOnClickListener(v -> validateAndSubmit());
    }

    private void initializeViews() {
        etNumber = findViewById(R.id.etnumber);
        etName = findViewById(R.id.etName);
        etDescriptionVeh = findViewById(R.id.etDescriptionveh);
        etPlacesVeh = findViewById(R.id.etplacesveh);

        selectImageVehButton = findViewById(R.id.selectImagevehButton);
        vehIDButton = findViewById(R.id.VehIDButton);
        cardIDButton = findViewById(R.id.CardIDButton);
        btnPhoneLogin = findViewById(R.id.btnsendinfoveh);
        selectedImageVeh = findViewById(R.id.selectedImageVeh);
        selectedVehID = findViewById(R.id.selectedVehID);
        selectedCardID = findViewById(R.id.selectedCardID);

        // Set initial visibility to gone
        selectedImageVeh.setVisibility(View.GONE);
        selectedVehID.setVisibility(View.GONE);
        selectedCardID.setVisibility(View.GONE);
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
                openCamera(launcher); // Open camera for capturing image
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    // Separate method to handle camera opening
    private void openCamera(ActivityResultLauncher<String> launcher) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Launch the appropriate activity result based on the launcher
            if (launcher == selectImagevehLauncher) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else if (launcher == vehIDLauncher) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_VEH_ID);
            } else if (launcher == cardIDLauncher) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CARD_ID);
            }
        } else {
            Toast.makeText(this, "No camera app available", Toast.LENGTH_SHORT).show();
        }
    }

    // Launcher for vehicle image selection
    ActivityResultLauncher<String> selectImagevehLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        displayImage(uri, selectedImageVeh, selectImageVehButton);
                    }
                }
            });

    // Launcher for vehicle ID image selection
    ActivityResultLauncher<String> vehIDLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        displayImage(uri, selectedVehID, vehIDButton);
                    }
                }
            });

    // Launcher for card ID image selection
    ActivityResultLauncher<String> cardIDLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        displayImage(uri, selectedCardID, cardIDButton);
                    }
                }
            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    // Resize the captured image
                    Bitmap resizedBitmap = resizeBitmap(imageBitmap, 500, 200); // Resize to 300x300

                    switch (requestCode) {
                        case REQUEST_IMAGE_CAPTURE:
                            selectedImageVeh.setImageBitmap(resizedBitmap);
                            selectImageVehButton.setVisibility(View.GONE);
                            selectedImageVeh.setVisibility(View.VISIBLE);
                            break;
                        case REQUEST_IMAGE_VEH_ID:
                            selectedVehID.setImageBitmap(resizedBitmap);
                            vehIDButton.setVisibility(View.GONE);
                            selectedVehID.setVisibility(View.VISIBLE);
                            break;
                        case REQUEST_IMAGE_CARD_ID:
                            selectedCardID.setImageBitmap(resizedBitmap);
                            cardIDButton.setVisibility(View.GONE);
                            selectedCardID.setVisibility(View.VISIBLE);
                            break;
                    }
                } else {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void displayImage(Uri uri, ImageView imageView, Button button) {
        if (uri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                Bitmap resizedBitmap = resizeBitmap(bitmap, 300, 300); // Resize to 300x300
                imageView.setImageBitmap(resizedBitmap); // Set resized bitmap
                button.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) { // Check for the correct request code
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Camera permission is required to use the camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void validateAndSubmit() {
        String number = etNumber.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String description = etDescriptionVeh.getText().toString().trim();
        String places = etPlacesVeh.getText().toString().trim();

        if (number.isEmpty() || name.isEmpty() || description.isEmpty() || places.isEmpty() ||
                selectedImageVeh.getDrawable() == null || selectedVehID.getDrawable() == null ||
                selectedCardID.getDrawable() == null) {
            Toast.makeText(this, "All fields and images must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert images to Bitmap for further processing or upload
        BitmapDrawable resDrawable = (BitmapDrawable) selectedImageVeh.getDrawable();
        BitmapDrawable resIdDrawable = (BitmapDrawable) selectedVehID.getDrawable();
        BitmapDrawable cardIdDrawable = (BitmapDrawable) selectedCardID.getDrawable();

        if (resDrawable != null && resIdDrawable != null && cardIdDrawable != null) {
            Bitmap resBitmap = resDrawable.getBitmap();
            Bitmap resIdBitmap = resIdDrawable.getBitmap();
            Bitmap cardIdBitmap = cardIdDrawable.getBitmap();

            // Process or upload bitmaps as needed
            // For now, just show a success message
            Toast.makeText(this, "Information submitted successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignNewManActivity.this , VehicleInfoActivity.class);
            startActivity(intent);
        }
    }
    private Bitmap resizeBitmap(Bitmap originalBitmap, int width, int height) {
        return Bitmap.createScaledBitmap(originalBitmap, width, height, true);
    }

}
