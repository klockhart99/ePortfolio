/**
 * @author Kenneth Lockhart
 * Date: 12/13/2024
 */
package com.example.lockhartkenneth_project.screens;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.lockhartkenneth_project.Main;
import com.example.lockhartkenneth_project.R;
import com.example.lockhartkenneth_project.SMSAlert;
import com.example.lockhartkenneth_project.database.EventDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handles all functions of editing an event
 */
public class Event extends AppCompatActivity {
    public static String fillInDate = ""; // Next date to use when filling in an event
    private EditText name, desc, startDate, endDate, startTime, endTime; // All adjustable ext fields
    private Button saveButton, cancelButton; // All clickable buttons

    public static int rowId = -1; // Row to preload during the next event
    private EventDatabase db; // Event database

    private long requestCode; // Code for permissions


    /**
     * Assigns all fields and prepares the event activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        // assign fields
        name = findViewById(R.id.eventName);
        desc = findViewById(R.id.eventDesc);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
        db = new EventDatabase(this); // Get database

        // Check if a date should be filled in
        if (!fillInDate.equals("")){
            // If the date isn't blank set start and end date to the fill in date and reset it
            startDate.setText(fillInDate);
            endDate.setText(fillInDate);
            fillInDate = "";
        }

        // checks if the rowId is valid
        if (rowId >= 0){
            // If valid get date at the row and assign it to the fields
            Cursor cursor = db.getEventData(rowId);
            if (cursor.getCount() > 0){
                cursor.moveToNext();
                name.setText(cursor.getString(2));
                desc.setText(cursor.getString(3));
                startDate.setText(cursor.getString(4).split("@")[0].strip());
                endDate.setText(cursor.getString(5).split("@")[0].strip());
                startTime.setText(cursor.getString(4).split("@")[1].strip());
                endTime.setText(cursor.getString(5).split("@")[1].strip());
            }
        }

        // Assign click listener to save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If all fields are filled in prepare to save event
                if (!name.getText().equals("") && !desc.getText().equals("") && !startDate.getText().equals("") && !endDate.getText().equals("")){
                    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mma"); // Date and time format
                    try {
                        // Try to take date from start and end date/time and format them to a Date. On failure catch the error and inform the user
                        Date start = formatter.parse(startDate.getText().toString() + " " + startTime.getText().toString()); // Start date format
                        Date end = formatter.parse(endDate.getText().toString() + " " + endTime.getText().toString()); // End date format

                        // Check if the row id contains data and that the end date is after or equal to the start date
                        if (db.getEventData(rowId).getCount() > 0 && (end.after(start) || start.equals(end))){
                            requestCode = rowId; // Request code equal row id
                            db.updateEvent(
                                    rowId+"",
                                    name.getText().toString(),
                                    desc.getText().toString(),
                                    startDate.getText().toString() + " @ " + startTime.getText().toString(),
                                    endDate.getText().toString() + " @ " + endTime.getText().toString());
                            addReminder(name.getText().toString(), desc.getText().toString(), startDate.getText().toString()  + " " + startTime.getText().toString()); // Add the reminder
                        // Check if the start date is before or equal to the end date
                        }else if ((end.after(start) || start.equals(end))){
                            requestCode = db.addEvent( // Request code equal returned value
                                    Main.usernameLoggedIn,
                                    name.getText().toString(),
                                    desc.getText().toString(),
                                    startDate.getText().toString() + " @ " + startTime.getText().toString(),
                                    endDate.getText().toString() + " @ " + endTime.getText().toString());
                            addReminder(name.getText().toString(), desc.getText().toString(), startDate.getText().toString() + " " + startTime.getText().toString()); // Add the reminder
                        }else{
                            // All other cases the date and time are incorrect. Inform the user and prevent switching screens
                            Toast.makeText(Event.this, "End date and time can not be before start date and time.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // If the save goes through switch screens back to the home screen.
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);
                        rowId = -1;
                    }catch (ParseException e){
                        // Clarification when format errors occur.
                        Toast.makeText(Event.this, "Invalid Data/Time Format. Use Date Format: MM-dd-yyyy (ex 12-22-2024)", Toast.LENGTH_LONG).show();
                        Toast.makeText(Event.this, "Invalid Data/Time Format. Use Time Format: hh:mma (ex. 01:00pm)", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        // Assign cancel button listener
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to the home screen and discard any changes
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Adds a reminder for start dates of events
     * @param eventName event name
     * @param eventDesc event description
     * @param eventTime event start time
     */
    private void addReminder(String eventName, String eventDesc, String eventTime) {
        if (ContextCompat.checkSelfPermission(Event.this,
            android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                // Set reminder timer
                setReminderTimer(eventName.trim(),
                        eventDesc.trim(),
                        eventTime.trim());
        } else{
            Toast.makeText(Event.this, "To add a reminder need to give SMS permission.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Sets up the notification reminder for events.
     * @param eventName event name
     * @param eventDesc event description
     * @param eventTime event start time
     */
    private void setReminderTimer(String eventName, String eventDesc, String eventTime) {
        // Create Intent
        Intent intent = new Intent(Event.this, SMSAlert.class);
        intent.putExtra("time", eventTime);
        intent.putExtra("event", eventName);
        intent.putExtra("description", eventDesc);

        // Create Pending Intent
        PendingIntent pending = PendingIntent.getBroadcast(
                Event.this,
                (int)requestCode,
                intent,
                PendingIntent.FLAG_MUTABLE);

        // Get alarm manager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        DateFormat format = new SimpleDateFormat("MM-dd-yyyy hh:mma"); // Date format
        try{
            Date dateToSet = format.parse(eventTime); // Parse string date to a Date
            assert dateToSet != null; // Insure date is not null

            // Set alarm to go off when the event starts
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    dateToSet.getTime(),
                    pending);

            // Tell the user about the reminder
            Toast.makeText(getApplicationContext(), "Reminder has been set.", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}
