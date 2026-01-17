package com.example.nocturnecinema;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OtpActivity extends AppCompatActivity {

    private EditText etOtp;
    private Button btnVerify;
    private int generatedOtp;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        etOtp = findViewById(R.id.et_otp);
        btnVerify = findViewById(R.id.btn_verify_otp);

        generatedOtp = getIntent().getIntExtra("OTP_CODE", -1);
        userId = getIntent().getIntExtra("USER_ID", -1);

        btnVerify.setOnClickListener(v -> verifyOtp());
    }

    private void verifyOtp() {
        String enteredOtpStr = etOtp.getText().toString().trim();

        if (TextUtils.isEmpty(enteredOtpStr)) {
            Toast.makeText(this, "OTP must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        int enteredOtp;
        try {
            enteredOtp = Integer.parseInt(enteredOtpStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid OTP format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (enteredOtp == generatedOtp) {
            // Save Login State
            SharedPreferences prefs = getSharedPreferences("NocturnePrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("USER_ID", userId);
            editor.putBoolean("IS_LOGGED_IN", true);
            editor.apply();

            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            
            Intent intent = new Intent(OtpActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid OTP Code", Toast.LENGTH_SHORT).show();
        }
    }
}
