package com.example.nocturnecinema.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nocturnecinema.models.Film;
import com.example.nocturnecinema.models.Transaction;
import com.example.nocturnecinema.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NocturneCinema.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_FILMS = "films";
    private static final String TABLE_TRANSACTIONS = "transactions";

    // User Cols
    private static final String COL_USER_ID = "id";
    private static final String COL_USER_NAME = "username";
    private static final String COL_USER_PASS = "password";
    private static final String COL_USER_EMAIL = "email";
    private static final String COL_USER_PHONE = "phone";

    // Film Cols
    private static final String COL_FILM_ID = "id";
    private static final String COL_FILM_TITLE = "title";
    private static final String COL_FILM_RATING = "rating";
    private static final String COL_FILM_COUNTRY = "country";
    private static final String COL_FILM_PRICE = "price";
    private static final String COL_FILM_DESC = "description";
    private static final String COL_FILM_COVER = "cover_url";

    // Transaction Cols
    private static final String COL_TRANS_ID = "id";
    private static final String COL_TRANS_USER_ID = "user_id";
    private static final String COL_TRANS_FILM_ID = "film_id";
    private static final String COL_TRANS_QTY = "quantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS = "CREATE TABLE " + TABLE_USERS + "("
                + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_USER_NAME + " TEXT,"
                + COL_USER_PASS + " TEXT,"
                + COL_USER_EMAIL + " TEXT,"
                + COL_USER_PHONE + " TEXT" + ")";

        String CREATE_FILMS = "CREATE TABLE " + TABLE_FILMS + "("
                + COL_FILM_ID + " INTEGER PRIMARY KEY," // From JSON ID
                + COL_FILM_TITLE + " TEXT,"
                + COL_FILM_RATING + " REAL,"
                + COL_FILM_COUNTRY + " TEXT,"
                + COL_FILM_PRICE + " INTEGER,"
                + COL_FILM_DESC + " TEXT,"
                + COL_FILM_COVER + " TEXT" + ")";

        String CREATE_TRANS = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                + COL_TRANS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TRANS_USER_ID + " INTEGER,"
                + COL_TRANS_FILM_ID + " INTEGER,"
                + COL_TRANS_QTY + " INTEGER,"
                + "FOREIGN KEY(" + COL_TRANS_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "),"
                + "FOREIGN KEY(" + COL_TRANS_FILM_ID + ") REFERENCES " + TABLE_FILMS + "(" + COL_FILM_ID + ")" + ")";

        db.execSQL(CREATE_USERS);
        db.execSQL(CREATE_FILMS);
        db.execSQL(CREATE_TRANS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    // --- USER ---
    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_USER_ID}, 
                COL_USER_NAME + "=?", new String[]{username}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public long registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, user.getUsername());
        values.put(COL_USER_PASS, user.getPassword());
        values.put(COL_USER_EMAIL, user.getEmail());
        values.put(COL_USER_PHONE, user.getPhone());
        return db.insert(TABLE_USERS, null, values);
    }

    public User loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COL_USER_NAME + "=? AND " + COL_USER_PASS + "=?",
                new String[]{username, password}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PASS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PHONE))
            );
            cursor.close();
            return user;
        }
        return null;
    }

    // --- FILM ---
    public void insertFilm(Film film) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FILM_ID, film.getId());
        values.put(COL_FILM_TITLE, film.getTitle());
        values.put(COL_FILM_RATING, film.getRating());
        values.put(COL_FILM_COUNTRY, film.getCountry());
        values.put(COL_FILM_PRICE, film.getPrice());
        values.put(COL_FILM_DESC, film.getDescription());
        values.put(COL_FILM_COVER, film.getCoverUrl());
        db.insertWithOnConflict(TABLE_FILMS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    
    public int getFilmCount() {
        String countQuery = "SELECT * FROM " + TABLE_FILMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public List<Film> getAllFilms() {
        List<Film> list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FILMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Film film = new Film(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_FILM_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_FILM_TITLE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_FILM_RATING)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_FILM_COUNTRY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_FILM_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_FILM_DESC)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_FILM_COVER))
                );
                list.add(film);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // --- TRANSACTION ---
    public long insertTransaction(int userId, int filmId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TRANS_USER_ID, userId);
        values.put(COL_TRANS_FILM_ID, filmId);
        values.put(COL_TRANS_QTY, quantity);
        return db.insert(TABLE_TRANSACTIONS, null, values);
    }

    public List<Transaction> getUserTransactions(int userId) {
        List<Transaction> list = new ArrayList<>();
        String query = "SELECT t." + COL_TRANS_ID + ", t." + COL_TRANS_USER_ID + ", t." + COL_TRANS_FILM_ID + 
                       ", t." + COL_TRANS_QTY + 
                       ", f." + COL_FILM_TITLE + ", f." + COL_FILM_RATING + ", f." + COL_FILM_COUNTRY + 
                       ", f." + COL_FILM_PRICE + ", f." + COL_FILM_COVER +
                       " FROM " + TABLE_TRANSACTIONS + " t " +
                       " JOIN " + TABLE_FILMS + " f ON t." + COL_TRANS_FILM_ID + " = f." + COL_FILM_ID +
                       " WHERE t." + COL_TRANS_USER_ID + " = ?";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Transaction t = new Transaction(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_TRANS_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_TRANS_USER_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_TRANS_FILM_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_TRANS_QTY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_FILM_TITLE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COL_FILM_RATING)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_FILM_COUNTRY)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_FILM_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_FILM_COVER))
                );
                list.add(t);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    
    public int updateTransaction(int transId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TRANS_QTY, newQuantity);
        return db.update(TABLE_TRANSACTIONS, values, COL_TRANS_ID + "=?", new String[]{String.valueOf(transId)});
    }
    
    public int deleteTransaction(int transId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TRANSACTIONS, COL_TRANS_ID + "=?", new String[]{String.valueOf(transId)});
    }
}
