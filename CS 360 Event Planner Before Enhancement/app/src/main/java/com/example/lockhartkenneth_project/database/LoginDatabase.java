/**
 * @author Kenneth Lockhart
 * Date: 12/13/2024
 */
package com.example.lockhartkenneth_project.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * Class that holds all data related to login database
 */
public class LoginDatabase extends SQLiteOpenHelper {
    public static final String DBNAME = "UserLoginDB"; // Name of database
    private final Context context;

    /**
     * Creates a login database
     * @param context the current referenced context if there is one
     */
    public LoginDatabase(@Nullable Context context) {
        // Create Database
        super(context, "UserLoginDB", null, 1);
        this.context = context;
    }

    /**
     * Ran on creation and makes the users table
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // On create make table users for log in data
        db.execSQL("create Table users(username TEXT primary key, password TEXT)");
    }

    /**
     * Ran on upgrade and drops the users table
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // When upgrading drop the existing table of users
        db.execSQL("drop Table if exists users");
    }

    /**
     * Inserts a user when given a username and password
     * @param username username
     * @param password password
     * @return true or false if it was successful or not
     */
    public Boolean insertUserInfo(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase(); // Get Database
        ContentValues Values = new ContentValues(); // Create a Content Value
        Values.put("username", username); // Add user
        Values.put("password", password); // Add password
        long result = db.insert("users", null, Values); // Insert values into table users

        return result != -1; // True if successful else false
    }

    /**
     * Check if the username is taken or not
     * @param username username to validate
     * @return username found or not
     */
    public Boolean checkUsername(String username){
        SQLiteDatabase db = this.getWritableDatabase(); // Get Database
        Cursor cursor = db.rawQuery("Select * from users where username = ?", new String[] {username}); // Query for username in database
        return cursor.getCount() > 0; // If data is found return true
    }

    /**
     * Validates that a user exist with the entered password
     * @param username username to validate
     * @param password password to validate with username
     * @return true or false if the login information is found or not found
     */
    public Boolean validateUserLogin(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase(); // Get Database
        Cursor cursor = db.rawQuery("Select * from users where username = ? and password = ?", new String[] {username, password});  // Query for username and password in database
        return cursor.getCount() > 0; // If data is found return true
    }

    /**
     * Deletes a user from the database
     * @param username user to delete
     */
    public void deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get Database
        long res = db.delete("users", "username=?", new String[]{username}); // Delete all users that match this username
    }
}
