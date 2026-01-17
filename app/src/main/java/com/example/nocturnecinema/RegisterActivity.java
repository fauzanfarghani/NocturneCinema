package com.example.nocturnecinema;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nocturnecinema.db.DatabaseHelper;
import com.example.nocturnecinema.models.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etEmail, etPhone;
    private Button btnRegister;
    private TextView tvLoginLink;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);
        etUsername = findViewById(R.id.et_reg_username);
        etPassword = findViewById(R.id.et_reg_password);
        etEmail = findViewById(R.id.et_reg_email);
        etPhone = findViewById(R.id.et_reg_phone);
        btnRegister = findViewById(R.id.btn_register);
        tvLoginLink = findViewById(R.id.tv_login_link);

        btnRegister.setOnClickListener(v -> handleRegister());

        tvLoginLink.setOnClickListener(v -> {
            finish();
        });
    }

    private void handleRegister() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || 
            TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.checkUsernameExists(username)) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 5) {
            Toast.makeText(this, "Password must be at least 5 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.endsWith(".com")) {
            Toast.makeText(this, "Email must end with .com", Toast.LENGTH_SHORT).show();
            return;
        }

        User newUser = new User(username, password, email, phone);
        long result = dbHelper.registerUser(newUser);

        if (result > 0) {
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            finish(); // Go back to login
        } else {
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
