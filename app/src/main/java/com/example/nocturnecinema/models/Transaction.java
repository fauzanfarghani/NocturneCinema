package com.example.nocturnecinema.models;

public class Transaction {
    private int id;
    private int userId;
    private int filmId;
    private int quantity;
    
    // Joined fields for display
    private String filmTitle;
    private double filmRating;
    private String filmCountry;
    private int filmPrice;
    private String filmCoverUrl;

    public Transaction(int id, int userId, int filmId, int quantity) {
        this.id = id;
        this.userId = userId;
        this.filmId = filmId;
        this.quantity = quantity;
    }
    
    // Constructor for joined data
    public Transaction(int id, int userId, int filmId, int quantity, String filmTitle, double filmRating, String filmCountry, int filmPrice, String filmCoverUrl) {
        this.id = id;
        this.userId = userId;
        this.filmId = filmId;
        this.quantity = quantity;
        this.filmTitle = filmTitle;
        this.filmRating = filmRating;
        this.filmCountry = filmCountry;
        this.filmPrice = filmPrice;
        this.filmCoverUrl = filmCoverUrl;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getFilmId() { return filmId; }
    public int getQuantity() { return quantity; }
    
    public String getFilmTitle() { return filmTitle; }
    public double getFilmRating() { return filmRating; }
    public String getFilmCountry() { return filmCountry; }
    public int getFilmPrice() { return filmPrice; }
    public String getFilmCoverUrl() { return filmCoverUrl; }
    
    public void setQuantity(int q) { this.quantity = q; }
}
