package com.example.nocturnecinema.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.nocturnecinema.R;
import com.example.nocturnecinema.adapters.FilmAdapter;
import com.example.nocturnecinema.db.DatabaseHelper;
import com.example.nocturnecinema.models.Film;
import com.example.nocturnecinema.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvFilms;
    private DatabaseHelper dbHelper;
    private FilmAdapter adapter;
    private static final String FILM_URL = "https://mocki.io/v1/ce4395c2-d593-45ae-b392-78fe2238369c";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        rvFilms = view.findViewById(R.id.rv_films);
        rvFilms.setLayoutManager(new LinearLayoutManager(getContext()));
        
        dbHelper = new DatabaseHelper(getContext());
        
        loadFilms();
        
        return view;
    }

    private void loadFilms() {
        if (dbHelper.getFilmCount() == 0) {
            // API link is confirmed Broken (404), using local assets instead
            loadFilmsFromAssets();
        } else {
            displayFilms();
        }
    }

    private void loadFilmsFromAssets() {
        try {
            java.io.InputStream is = getContext().getAssets().open("films.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            
            org.json.JSONArray response = new org.json.JSONArray(json);
            
            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = response.getJSONObject(i);
                Film film = new Film(
                    obj.getInt("id"),
                    obj.getString("title"),
                    obj.getDouble("rating"),
                    obj.getString("country"),
                    obj.getInt("price"),
                    obj.getString("description"),
                    obj.getString("cover_url")
                );
                dbHelper.insertFilm(film);
            }
            displayFilms();
            
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error loading local data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void fetchFilmsFromApi() {
        // API Broken, deactivated to prevent errors.
        loadFilmsFromAssets();
    }

    private void displayFilms() {
        List<Film> list = dbHelper.getAllFilms();
        adapter = new FilmAdapter(getContext(), list);
        rvFilms.setAdapter(adapter);
    }
}
