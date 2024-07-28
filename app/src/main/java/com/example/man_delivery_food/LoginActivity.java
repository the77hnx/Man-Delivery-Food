package com.example.man_delivery_food;

import static android.accounts.AccountManager.KEY_PASSWORD;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhoneNumber, etPasslogin;
    private CheckBox checkboxRememberMe;
    private Button btnPhoneLogin, btnOpenAccount;

    private static final String PREF_NAME = "login_preferences";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_REMEMBER_ME = "remember_me";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPhoneNumber = findViewById(R.id.etnumberlogin);
        etPasslogin = findViewById(R.id.etPasslogin);
        checkboxRememberMe = findViewById(R.id.checkboxRememberMe);
        btnPhoneLogin = findViewById(R.id.btnPhoneLoginpage);
        btnOpenAccount = findViewById(R.id.btnopenaccount);

        btnPhoneLogin.setOnClickListener(v -> {
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            String password = etPasslogin.getText().toString().trim();

            if (phoneNumber.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "الرجاء إدخال جميع الحقول", Toast.LENGTH_SHORT).show();
            } else {
                // Simulate login check (replace with your actual login logic)
                boolean isLoginValid = isLoginValid(phoneNumber, password);

                if (isLoginValid) {
                    // Save login info if "Remember Me" is checked
                    if (checkboxRememberMe.isChecked()) {
                        saveLoginPreferences(phoneNumber, password, true);
                    } else {
                        clearLoginPreferences();
                    }

                    // Navigate to OTP page (replace with your OTP activity)
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Finish current activity to prevent back button from returning here
                } else {
                    Toast.makeText(LoginActivity.this, "بيانات تسجيل الدخول غير صحيحة", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Load saved login info if "Remember Me" was previously checked
        if (loadLoginPreferences()) {
            String savedPhoneNumber = getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(KEY_PHONE_NUMBER, "");
            String savedPassword = getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(KEY_PASSWORD, "");

            etPhoneNumber.setText(savedPhoneNumber);
            etPasslogin.setText(savedPassword);
            checkboxRememberMe.setChecked(true);
        }

        btnOpenAccount.setOnClickListener(v -> {
            // Handle open account request (replace with your actual logic)

            Intent intent = new Intent(LoginActivity.this, SignNewManActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean isLoginValid(String phoneNumber, String email) {
        // Replace with your actual login validation logic (e.g., check against a database)
        // This is a placeholder method
        return true; // For demo purposes, always return true
    }



    private void saveLoginPreferences(String phoneNumber, String email, boolean rememberMe) {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_PHONE_NUMBER, phoneNumber);
        editor.putString(KEY_PASSWORD, email);
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe);
        editor.apply();
    }

    private boolean loadLoginPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
    }

    private void clearLoginPreferences() {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.clear().apply();
    }
}
