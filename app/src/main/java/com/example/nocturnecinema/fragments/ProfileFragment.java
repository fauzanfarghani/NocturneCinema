package com.example.nocturnecinema.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nocturnecinema.AboutUsActivity;
import com.example.nocturnecinema.LoginActivity;
import com.example.nocturnecinema.R;
import com.example.nocturnecinema.db.DatabaseHelper;

public class ProfileFragment extends Fragment {

    private TextView tvUsername, tvEmail, tvPhone;
    private Button btnLogout, btnAboutUs;
    private DatabaseHelper dbHelper;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        tvUsername = view.findViewById(R.id.tv_profile_username);
        tvEmail = view.findViewById(R.id.tv_profile_email);
        tvPhone = view.findViewById(R.id.tv_profile_phone);
        btnLogout = view.findViewById(R.id.btn_logout);
        btnAboutUs = view.findViewById(R.id.btn_about_us);
        
        dbHelper = new DatabaseHelper(getContext());
        
        SharedPreferences prefs = getContext().getSharedPreferences("NocturnePrefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("USER_ID", -1);
        
        loadUserProfile();
        
        btnLogout.setOnClickListener(v -> showLogoutDialog());
        btnAboutUs.setOnClickListener(v -> startActivity(new Intent(getContext(), AboutUsActivity.class)));
        
        return view;
    }

    private void loadUserProfile() {
        if (userId != -1) {
            // Need a method to get User by ID in DbHelper, or just query here.
            // For now I'll just query manually since I didn't add getUserById in Helper.
            // Actually it's cleaner to add it to Helper but raw query works for this assignment.
            // Let's use raw query for speed or add to helper. I'll add a quick query here.
            
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{String.valueOf(userId)});
            if (cursor.moveToFirst()) {
               tvUsername.setText(cursor.getString(cursor.getColumnIndexOrThrow("username")));
               tvEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
               tvPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
            }
            cursor.close();
        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_logout_confirmation, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        android.widget.Button btnCancel = view.findViewById(R.id.btn_dialog_cancel);
        android.widget.Button btnLogoutConfirm = view.findViewById(R.id.btn_dialog_logout);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnLogoutConfirm.setOnClickListener(v -> {
            SharedPreferences prefs = getContext().getSharedPreferences("NocturnePrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
            dialog.dismiss();
        });

        dialog.show();
    }
}
