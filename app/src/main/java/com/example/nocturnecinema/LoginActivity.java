package com.example.nocturnecinema;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nocturnecinema.db.DatabaseHelper;
import com.example.nocturnecinema.models.User;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegisterLink;
    private DatabaseHelper dbHelper;
    private static final int SMS_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegisterLink = findViewById(R.id.tv_register_link);
        
        // Request SMS permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }

        btnLogin.setOnClickListener(v -> handleLogin());

        tvRegisterLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Username must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = dbHelper.loginUser(username, password);

        if (user != null) {
            // Generate OTP
            int otp = new Random().nextInt(9000) + 1000; // 1000-9999
            
            // Send SMS
            String message = "Your OTP is: " + otp;
            
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(user.getPhone(), null, message, null, null);
                Toast.makeText(this, "OTP Sent to " + user.getPhone(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to send SMS (Simulated)", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
            intent.putExtra("USER_ID", user.getId());
            intent.putExtra("OTP_CODE", otp);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
        }
    }
}
