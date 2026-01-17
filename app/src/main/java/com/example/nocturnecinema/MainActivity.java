package com.example.nocturnecinema;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.nocturnecinema.fragments.HomeFragment;
import com.example.nocturnecinema.fragments.ProfileFragment;
import com.example.nocturnecinema.fragments.TransactionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check Login Status
        android.content.SharedPreferences prefs = getSharedPreferences("NocturnePrefs", android.content.Context.MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("IS_LOGGED_IN", false);
        
        if (!isLoggedIn) {
            android.content.Intent intent = new android.content.Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        
        // Load default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_transactions) {
                selectedFragment = new TransactionFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }
            
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            }
            return true;
        });
    }
}
