/**
 * @author Kenneth Lockhart
 * Date: 03/23/2025
 */
package com.example.lockhartkenneth_project.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lockhartkenneth_project.Main;
import com.example.lockhartkenneth_project.R;
import com.example.lockhartkenneth_project.database.EventDatabase;
import com.example.lockhartkenneth_project.screens.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyPlanner#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyPlanner extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<String> eventTitles, eventDescs, eventStartDates, eventEndDates, eventIds;
    private ArrayList<Integer> usedHoursEventList1;
    private ArrayList<Integer> usedHoursEventList2;
    private Date currentDay;
    private TextView currentDayView;
    private ConstraintLayout eventList1;
    private ConstraintLayout eventList2;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy"); // Date format for dates and time
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mma"); // Date format for dates and time

    public DailyPlanner() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyPlanner.
     */
    public static DailyPlanner newInstance(String param1, String param2) {
        DailyPlanner fragment = new DailyPlanner();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * On create prepare arguments
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Not used part of Fragment
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_planner, container, false);

        // Assign the current day to today
        currentDayView = view.findViewById(R.id.currentDayText);
        currentDay = new Date();
        currentDay.setTime(System.currentTimeMillis()); // sets current time to a value

        // Try to parse the date, this should never fail
        try {
            currentDay = dateFormatter.parse(dateFormatter.format(currentDay)); // formats the date correctly
            currentDayView.setText(dateFormatter.format(currentDay));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Creates listener for the add new event button
        view.findViewById(R.id.calandarAddEventButtonDaily).setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Event.class);
            Event.fillInDate = currentDayView.getText().toString(); // give event the date value for start and end date
            startActivity(intent); // Go to event screen
        });

        // Create button listener to got to the previous day
        Button prevButton = view.findViewById(R.id.previousDayButton);
        prevButton.setOnClickListener(v -> {
            // Change date back 24 hours
            currentDay.setTime(currentDay.getTime() - 24L * 60L * 60L * 1000L);
            // Try to parse change, this should never fail
            try {
                currentDay = dateFormatter.parse(dateFormatter.format(currentDay)); // formats the date correctly
                currentDayView.setText(dateFormatter.format(currentDay));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            // Updates the current view and data
            storeDataInArrays(view);
            createEvents(inflater);
        });
        Button nextButton = view.findViewById(R.id.nextDayButton);
        nextButton.setOnClickListener(v -> {
            // Change date ahead 24 hours
            currentDay.setTime(currentDay.getTime() + 24L * 60L * 60L * 1000L);
            // Try to parse change, this should never fail
            try {
                currentDay = dateFormatter.parse(dateFormatter.format(currentDay)); // formats the date correctly
                currentDayView.setText(dateFormatter.format(currentDay));
            } catch (ParseException e) {
                Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
            }

            // Updates the current view and data
            storeDataInArrays(view);
            createEvents(inflater);

        });

        // Create holders
        eventIds = new ArrayList<>();
        eventTitles = new ArrayList<>();
        eventDescs = new ArrayList<>();
        eventStartDates = new ArrayList<>();
        eventEndDates = new ArrayList<>();
        usedHoursEventList1 = new ArrayList<>();
        usedHoursEventList2 = new ArrayList<>();

        // Get event lists
        eventList1 = view.findViewById(R.id.eventList1);
        eventList2 = view.findViewById(R.id.eventList2);

        // Creates Initial View for current day
        storeDataInArrays(view);
        createEvents(inflater);

        return view;
    }

    private void createEvents(LayoutInflater inflater) {
        final float scale = requireContext().getResources().getDisplayMetrics().density; // Gets dp value

        // Clear and remove all views and event restrictions
        eventList1.removeAllViews();
        eventList2.removeAllViews();
        usedHoursEventList1.clear();
        usedHoursEventList2.clear();

        // Prepare the event lists
        eventList1.setMinHeight((int) (64 * 24 * scale + 0.5f));
        eventList2.setMinHeight((int) (64 * 24 * scale + 0.5f));
        eventList1.setMaxHeight((int) (64 * 24 * scale + 0.5f));
        eventList2.setMaxHeight((int) (64 * 24 * scale + 0.5f));

        // For each event make an event view
        for (int i = 0; i < eventIds.size(); i++) {
            // Try to create the event
            try {

                // Create variables
                View event;
                ConstraintLayout target;

                // Convert start and end from strings to dates
                Date startTime = timeFormatter.parse(eventStartDates.get(i).split("@")[1].strip()); // formats the start date
                Date endTime = timeFormatter.parse(eventEndDates.get(i).split("@")[1].strip()); // formats the end date
                Date dStartDate = dateFormatter.parse(eventStartDates.get(i).split("@")[0].strip()); // formats the start date
                Date dEndDate = dateFormatter.parse(eventEndDates.get(i).split("@")[0].strip()); // formats the start date


                assert dStartDate != null;
                assert dEndDate != null;
                assert startTime != null;
                assert endTime != null;

                boolean isStartToday = currentDay.getTime() == dStartDate.getTime(); // Checks if the start day is the current day
                boolean afterStart = currentDay.after(dStartDate); // Checks if the current day is after the start date
                boolean beforeEnd = currentDay.before(dEndDate); // Checks if the current day is before the end date
                boolean isEndToday = currentDay.getTime() == dEndDate.getTime(); // Checks if the end date is the current day

                float timeStartInHours = 0.0f;
                float timeEndInHours = 24.0f;

                // Used to negate the effects of timezones and daylight saving time allowing the application to be used in any timezone
                long timezone = timeFormatter.getTimeZone().getOffset(currentDay.getTime());
                long dst = timeFormatter.getTimeZone().getDSTSavings();

                // If the event started today then assign time start hour
                if (isStartToday){
                    timeStartInHours = (float) (startTime.getTime() + timezone - dst) / (1000L * 60L * 60L);
                }

                // If the event ends today then assign time end hour
                if (isEndToday){
                    timeEndInHours = (float) (endTime.getTime() + timezone - dst) / (1000L * 60L * 60L);
                }

                // If the event is both after the start and before the end then assign event to all day
                if (afterStart && beforeEnd){
                    timeStartInHours = 0.0f;
                    timeEndInHours = 24.0f;
                }

                // Generates the lock hours for the used time slots
                int startHour = (int) Math.floor(timeStartInHours*100f);
                int endHour = (int) Math.floor(timeEndInHours*100f);

                // Gets the size factor for the event
                float timeOfBlock = timeEndInHours - timeStartInHours;

                // Assigns a ypos and ysize using time start and time block. Note 64dp = 1 hour thus 64 * hours * scale = ypos or ysize
                int ypos = (int) (64 * timeStartInHours * scale + 0.5f + 0 * scale);
                int ysize = (int) (64 * timeOfBlock * scale + 0.5f + 0 * scale);

                // Check if the eventlist1 has the block used
                boolean eventList1HourUsed = isEventHourUsed(usedHoursEventList1, startHour, endHour);
                boolean eventList2HourUsed = false;

                // If eventlist1 doesn't have room check eventlist
                if (eventList1HourUsed){
                    // Check if the eventlist2 has the block used
                    eventList2HourUsed = isEventHourUsed(usedHoursEventList2, startHour, endHour);
                }

                if (!eventList1HourUsed){
                    // If eventlist1 has room inflate the event
                    event = inflater.inflate(R.layout.event_daily, eventList1, false);
                    target = eventList1; // Set target to eventlist1
                    assignEventHours(usedHoursEventList1, startHour, endHour); // Lock the block by assigning hours used to eventlist1
                }else if (!eventList2HourUsed){
                    // If eventlist2 has room inflate the event
                    event = inflater.inflate(R.layout.event_daily, eventList2, false);
                    target = eventList2; // Set target to eventlist2
                    assignEventHours(usedHoursEventList2, startHour, endHour); // Lock the block by assigning hours used to eventlist2
                }else{
                    // If neither list has room do assign event and target to null
                    event = null;
                    target = null;
                    // Tell the user that some events can not be displayed
                    Toast.makeText(getContext(),"Some events could not be displayed!", Toast.LENGTH_LONG).show();
                }

                // If an event exists and a target exist create the event view and add it to the target
                if (event != null && target != null){
                    // Gets the row id from database
                    String rowId = eventIds.get(i);

                    // Gets Text fields and assigns them correct values
                    TextView name = event.findViewById(R.id.eventNameDaily);
                    TextView desc = event.findViewById(R.id.eventDescDaily);
                    name.setText(eventTitles.get(i));
                    desc.setText(eventDescs.get(i));

                    // Set the event positions
                    event.setY(ypos);

                    // Creates the event edit listener to send the user to edit when clicked
                    event.findViewById(R.id.editEventButton).setOnClickListener((view -> {
                        // On edit send row data to Event and switch to event screen
                        Intent intent = new Intent(getContext(), Event.class);
                        Event.rowId = Integer.parseInt(rowId);
                        this.startActivity(intent);
                    }));
                    // Creates the event delete listener to remove the event and update the target view
                    event.findViewById(R.id.deleteEventButton).setOnClickListener((view -> {
                        // On delete remove the row data and view
                        EventDatabase db = new EventDatabase(view.getContext());
                        db.deleteSingleRowEvent(rowId); // Delete single row
                        target.removeView(event);
                    }));

                    // Add the event to the target in the correct size.
                    target.addView(event, target.getWidth(), ysize);
                }

            } catch (ParseException e) {
                Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Assigns the time from start hour to end hour as used for the specific array
     * @param usedHoursEventList array of integers
     * @param startHour start hour
     * @param endHour end hour
     */
    private void assignEventHours(ArrayList<Integer> usedHoursEventList, int startHour, int endHour) {
        // Locks the following hours from start to end
        for (int j = startHour; j < endHour; j++){
            usedHoursEventList.add(j);
        }
    }

    /**
     * Checks if the hour has been used in the specific array
     * @param usedHoursEventList array of integers
     * @param startHour start hour
     * @param endHour end hour
     * @return true or false if the event falls in used hours
     */
    private boolean isEventHourUsed(ArrayList<Integer> usedHoursEventList, int startHour, int endHour) {
        // Checks the locks on the following hours from start to end
        for (int j = startHour; j < endHour; j++){
            if (usedHoursEventList.contains(j)){
                return true;
            }
        }
        return false;
    }


    /**
     * Stores the correct values for the user and events in the current day
     * @param view view to use for reference
     */
    private void storeDataInArrays(View view){
        // Clear event holders
        eventIds.clear();
        eventTitles.clear();
        eventDescs.clear();
        eventStartDates.clear();
        eventEndDates.clear();

        EventDatabase db = new EventDatabase(view.getContext()); // Get database
        Cursor cursor = db.getAllEventsForLoggedInUser(); // Get result of all events for current user

        if(cursor.getCount() != 0){
            // If data was found loop until all data has been accounted for
            while (cursor.moveToNext()){
                String databaseDateStart = cursor.getString(4).split("@")[0].strip(); // Get the starting date in the correct format
                String databaseDateEnd = cursor.getString(5).split("@")[0].strip(); // Get the ending date in the correct format
                try {
                    boolean usernameCorrect = cursor.getString(1).equals(Main.usernameLoggedIn); // Checks that the data return match the expect username
                    Date dStartDate = dateFormatter.parse(databaseDateStart); // formats the start date
                    Date dEndDate = dateFormatter.parse(databaseDateEnd); // formats the end date

                    assert dStartDate != null;
                    assert dEndDate != null;
                    boolean isStartToday = currentDay.getTime() == dStartDate.getTime(); // Checks if the start day is the current day
                    boolean afterStart = currentDay.after(dStartDate); // Checks if the current day is after the start date
                    boolean beforeEnd = currentDay.before(dEndDate); // Checks if the current day is before the end date
                    boolean isEndToday = currentDay.getTime() == dEndDate.getTime(); // Checks if the end date is the current day

                    // Checks if the username is correct and that at least the current day is the start, end or in between before storing the data
                    if (usernameCorrect && (isStartToday || isEndToday || (afterStart && beforeEnd))) {
                        // Assign values to holders
                        eventIds.add(cursor.getString(0));
                        eventTitles.add(cursor.getString(2));
                        eventDescs.add(cursor.getString(3));
                        eventStartDates.add(cursor.getString(4));
                        eventEndDates.add(cursor.getString(5));
                    }
                } catch (ParseException e) {
                    // In the event of an error tell the user of the error // this should not happen but it is handled if it does
                    Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}