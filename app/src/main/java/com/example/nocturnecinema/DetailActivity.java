package com.example.nocturnecinema;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nocturnecinema.db.DatabaseHelper;
import com.example.nocturnecinema.models.Film;

public class DetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvRating, tvCountry, tvPrice, tvDesc;
    private TextView tvQtyValue;
    private Button btnQtyMinus, btnQtyPlus;
    private int currentQty = 1;
    
    private Button btnBuy;
    private DatabaseHelper dbHelper;
    private Film film;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbHelper = new DatabaseHelper(this);
        
        tvTitle = findViewById(R.id.tv_detail_title);
        tvRating = findViewById(R.id.tv_detail_rating);
        tvCountry = findViewById(R.id.tv_detail_country);
        tvPrice = findViewById(R.id.tv_detail_price);
        tvDesc = findViewById(R.id.tv_detail_desc);
        
        tvQtyValue = findViewById(R.id.tv_buy_qty);
        btnQtyMinus = findViewById(R.id.btn_qty_minus);
        btnQtyPlus = findViewById(R.id.btn_qty_plus);
        
        btnBuy = findViewById(R.id.btn_buy);

        film = (Film) getIntent().getSerializableExtra("FILM_DATA");
        
        SharedPreferences prefs = getSharedPreferences("NocturnePrefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("USER_ID", -1);

        if (film != null) {
            tvTitle.setText(film.getTitle());
            tvRating.setText("Rating: " + film.getRating());
            tvCountry.setText("Country: " + film.getCountry());
            tvPrice.setText("Price: $" + film.getPrice());
            tvDesc.setText(film.getDescription());
            
            android.widget.ImageView ivCover = findViewById(R.id.iv_detail_cover);
            com.example.nocturnecinema.utils.ImageLoader.loadImage(film.getCoverUrl(), ivCover);
        }
        
        tvQtyValue.setText(String.valueOf(currentQty));

        btnQtyMinus.setOnClickListener(v -> {
            if (currentQty > 1) {
                currentQty--;
                tvQtyValue.setText(String.valueOf(currentQty));
            }
        });
        
        btnQtyPlus.setOnClickListener(v -> {
            currentQty++;
            tvQtyValue.setText(String.valueOf(currentQty));
        });

        btnBuy.setOnClickListener(v -> handleBuy());
    }

    private void handleBuy() {
        if (userId == -1) {
            Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
            return;
        }

        // currentQty is maintained by buttons, so it's always valid number >= 1
        // But for safety:
        if (currentQty <= 0) {
            Toast.makeText(this, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (film != null) {
            long result = dbHelper.insertTransaction(userId, film.getId(), currentQty);
            if (result > 0) {
                Toast.makeText(this, "Transaction Successful!", Toast.LENGTH_SHORT).show();
                finish(); // Close detail page
            } else {
                Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
