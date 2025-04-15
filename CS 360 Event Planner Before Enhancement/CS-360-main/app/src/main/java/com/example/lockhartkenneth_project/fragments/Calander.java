/**
 * @author Kenneth Lockhart
 * Date: 12/13/2024
 */
package com.example.lockhartkenneth_project.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.lockhartkenneth_project.CustomAdaptor;
import com.example.lockhartkenneth_project.Main;
import com.example.lockhartkenneth_project.R;
import com.example.lockhartkenneth_project.database.EventDatabase;
import com.example.lockhartkenneth_project.screens.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Calander#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Calander extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private EventDatabase db;

    private RecyclerView recyclerView;
    private CustomAdaptor customAdaptor;

    private CalendarView calendar;

    private String selectedDate;

    private ArrayList<String> eventTitles, eventDescs, eventStartDates, eventEndDates, eventIds;
    public Calander() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Calander.
     */
    public static Calander newInstance(String param1, String param2) {
        Calander fragment = new Calander();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * On create handle the parameters and create the save instance state
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Creates the view for the fragment
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return view of fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calander, container, false); // Gets the view
        // assign fields
        recyclerView = view.findViewById(R.id.calRecyclerView);
        calendar = view.findViewById(R.id.calendarItem);

        // Assign date change listener which updates the recycler and selected date
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate =  (month+1) + "-" + dayOfMonth + "-" + year; // New selected date
                updateRecycler(view); // Update recycler
            }
        });

        // assigns the floating add button the ability to add an event based on selected date.
        view.findViewById(R.id.calandarAddEventButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to event screen
                Intent intent = new Intent(v.getContext(), Event.class);
                Event.fillInDate = selectedDate; // give event the date value for start and end date
                startActivity(intent); // Switch Screens
            }
        });

        // Create first selected date
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy"); // format for dates
        long dateLong = calendar.getDate(); // get the date from calandar
        Date dateLongConverted = new Date(dateLong); // Convert the long into a date
        selectedDate = formatter.format(dateLongConverted); // Convert date to selectDate format

        // Create new event data lists
        eventIds = new ArrayList<>();
        eventTitles = new ArrayList<>();
        eventDescs = new ArrayList<>();
        eventStartDates = new ArrayList<>();
        eventEndDates = new ArrayList<>();


        // Update recycler
        updateRecycler(view);
        return view; // Return the view
    }

    /**
     * Handles updating all date related to the recycler
     * @param view view to modify
     */
    public void updateRecycler(View view) {
        // Store date inside the events
        storeDataInArrays(view);
        // Create adaptor for recycler
        customAdaptor = new CustomAdaptor(this,view.getContext(), eventIds, eventTitles,
                eventDescs, eventStartDates, eventEndDates);
        recyclerView.setAdapter(customAdaptor); // set the adaptor as used for recycle view
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext())); // Apply layout to recycler view
    }

    /**
     * Stores the correct values for the user and current date in the holders
     * @param view view to use for reference
     */
    private void storeDataInArrays(View view){
        // Empty event holders
        eventIds.clear();
        eventTitles.clear();
        eventDescs.clear();
        eventStartDates.clear();
        eventEndDates.clear();

        db = new EventDatabase(view.getContext()); // create datebase access
        Cursor cursor = db.getAllEventsForLoggedInUser(); // get the result of all events for the current user
        if(cursor.getCount() != 0){
            // If data was found loop until all data has been accounted for
            while (cursor.moveToNext()){
                String databaseDateEnd = cursor.getString(5).split("@")[0].strip(); // Get the ending date in the correct format
                String databaseDateStart = cursor.getString(4).split("@")[0].strip(); // Get the start date in the correct format
                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy"); // Date format for dates
                try {
                    boolean usernameCorrect = cursor.getString(1).equals(Main.usernameLoggedIn); // Checks that the data return match the expect username
                    Date dStartDate = formatter.parse(databaseDateStart); // formats the start date
                    Date dEndDate = formatter.parse(databaseDateEnd); // formats the end date
                    Date sDate = formatter.parse(selectedDate); // formats the current selected date
                    boolean beforeEndDate = sDate.before(dEndDate); // Checks if selected date is before end date
                    boolean afterStartDate = sDate.after(dStartDate); // Checks if selected date is after start date
                    boolean selectedIsStart = databaseDateStart.equals(selectedDate); // Checks if the start date matches the selected date
                    boolean selectedIsEnd = databaseDateEnd.equals(selectedDate); // Checks if the end date matches the selected date
                    // Checks If the username was correct and at least either the selected date matched the start/end date or fell in between start and end dates
                    if (usernameCorrect && ((beforeEndDate && afterStartDate) || selectedIsStart || selectedIsEnd)) {
                        // Assign data to holders
                        eventIds.add(cursor.getString(0));
                        eventTitles.add(cursor.getString(2));
                        eventDescs.add(cursor.getString(3));
                        eventStartDates.add(cursor.getString(4));
                        eventEndDates.add(cursor.getString(5));
                    }
                } catch (ParseException e) {
                    // In the event of an error tell the user of the error // this should not happen but it is handled if it does
                    Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG);
                }


                ;

            }
        }
    }
}