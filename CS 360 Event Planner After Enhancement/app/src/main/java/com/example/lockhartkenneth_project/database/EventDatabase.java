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

import com.example.lockhartkenneth_project.Main;

/**
 * Event Database for all stored events.
 */
public class EventDatabase extends SQLiteOpenHelper {

    // Database column and info constants
    private static final String tableName = "events";
    private static final String idC = "_id";
    private static final String username = "username";
    private static final String eventTitleC = "name";
    private static final String eventDescC = "description";
    private static final String startDateC = "start_date";
    private static final String endDateC = "end_date";
    private static final String DBNAME = "Event.db";
    private final Context context; // Context


    /**
     * Create an event database object
     * @param context context to use for database object
     */
    public EventDatabase(@Nullable Context context) {
        super(context, DBNAME, null, 1); // Call SQLightOpenHelper Constructor
        this.context = context; // assign context
    }

    /**
     * On creation of database create a table for events using the constants outlined in this object
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Builds initial sql statemnet
        String sql =
                "CREATE TABLE " + tableName + " (" +
                        idC + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Primary key for all events
                        username + " TEXT, " + // Username of the person who create an event. User to filter events for the current user only
                        eventTitleC + " TEXT, " + // The title column
                        eventDescC + " TEXT, " + // The description column
                        startDateC + " TEXT, " + // The start date and time column
                        endDateC + " TEXT);"; // The end date and time column
        db.execSQL(sql); // Run the sql code
    }

    /**
     * On upgrade drop the events table and create a new table
     * @param db The database.
     * @param i The old database version.
     * @param i1 The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
    }

    /**
     * Adds an event to the events table if possible
     * @param user username of the event
     * @param eventName name of the event
     * @param eventDesc description of the event
     * @param startDate start date and time of the event
     * @param endDate end date and time of the event
     * @return the result of the insert
     */
    public long addEvent(String user, String eventName, String eventDesc, String startDate, String endDate){
        SQLiteDatabase db = this.getWritableDatabase(); // Get the datebase
        ContentValues cv = new ContentValues(); // Create contect values

        cv.put(username, user); // Add user to values
        cv.put(eventTitleC, eventName); // Add event title to values
        cv.put(eventDescC, eventDesc); // Add event description to values
        cv.put(startDateC, startDate); // Add start date and time to values
        cv.put(endDateC, endDate); // Add end date and time to values

        long res = db.insert(tableName, null, cv); // Insert values into the database

        // Check if the insert was successful and informs the user of the result
        if (res == -1){
            Toast.makeText(context, "Failed to add event", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Event has been added.", Toast.LENGTH_SHORT).show();

        }
        return res;
    }

    /**
     * Update the event data
     * @param row_id row to update
     * @param eventName updated name of the event
     * @param eventDesc updated description of the event
     * @param startDate updated start date and time of the event
     * @param endDate updated end date and time of the event
     */
    public void updateEvent(String row_id, String eventName, String eventDesc, String startDate, String endDate){
        SQLiteDatabase db = this.getWritableDatabase(); // Get the datebase
        ContentValues cv = new ContentValues(); // Create contect values

        cv.put(eventTitleC, eventName); // Add event title to values
        cv.put(eventDescC, eventDesc); // Add event description to values
        cv.put(startDateC, startDate); // Add start date and time to values
        cv.put(endDateC, endDate); // Add end date and time to values

        long res = db.update(tableName, cv, "_id=?", new String[]{row_id});

        // Check if the update was successful and informs the user of the result
        if (res == -1){
            Toast.makeText(context, "Updating event failed!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Updating event success!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Delete an event
     * @param row of event to delete
     */
    public void deleteSingleRowEvent(String row){
        SQLiteDatabase db = this.getWritableDatabase(); // Get database
        long res = db.delete(tableName, "_id=?", new String[]{row}); // Delete all matching row numbers
        // Inform the user of the result from the deletion
        if(res == -1){
            Toast.makeText(context, "Failed to delete event!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Event successfully deleted!", Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * Gets the data from a given row id
     * @param rowId row id to get data from
     * @return Cursor to data
     */
    public Cursor getEventData(int rowId){
        SQLiteDatabase db = this.getWritableDatabase(); // Get Database
        String query = "SELECT * FROM " + tableName + " where _id = ?"; // Queue data where the row id matches the inputted row number
        // Run the query
        return db.rawQuery(query, new String[] {String.valueOf(rowId)}); // Return query result
    }

    /**
     * Query data for user by pulling all queues with the current user logged in.
     * @return cursor to data for user
     */
    public Cursor getAllEventsForLoggedInUser() {
        String q = "SELECT * FROM " + tableName + " where username = ?"; // query
        SQLiteDatabase db = this.getReadableDatabase(); // Get Database
        // run query
        return db.rawQuery(q, new String[]{Main.usernameLoggedIn}); // Return query result
    }

    /**
     * Deletes all data for event for the logged in user.
     * @param username user to delete all data of
     */
    public void deleteAllUserEvents(String username) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get database
        long res = db.delete(tableName, "username=?", new String[]{username}); // delete all result where the current username matches the user logged in.
    }
}
