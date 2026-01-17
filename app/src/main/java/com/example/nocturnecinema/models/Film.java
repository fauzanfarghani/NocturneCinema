package com.example.nocturnecinema.models;

import java.io.Serializable;

public class Film implements Serializable {
    private int id;
    private String title;
    private double rating;
    private String country;
    private int price;
    private String description;
    private String coverUrl;

    public Film(int id, String title, double rating, String country, int price, String description, String coverUrl) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.country = country;
        this.price = price;
        this.description = description;
        this.coverUrl = coverUrl;
    }
    
    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public double getRating() { return rating; }
    public String getCountry() { return country; }
    public int getPrice() { return price; }
    public String getDescription() { return description; }
    public String getCoverUrl() { return coverUrl; }
}
