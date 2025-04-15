/**
 * @author Kenneth Lockhart
 * Date: 12/13/2024
 */
package com.example.lockhartkenneth_project.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Use the {@link UpcomingEvents#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingEvents extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private EventDatabase db;

    private RecyclerView recyclerView;
    private CustomAdaptor customAdaptor;

    private ArrayList<String> eventTitles, eventDescs, eventStartDates, eventEndDates, eventIds;

    /**
     * Empty constructor since it is required
     */
    public UpcomingEvents() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpcomingEvents.
     */
    // TODO: Rename and change types and number of parameters
    public static UpcomingEvents newInstance(String param1, String param2) {
        UpcomingEvents fragment = new UpcomingEvents();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_events, container, false);  // Gets the view

        // Creates listener for the add new event button
        view.findViewById(R.id.addEventButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Event.class);
                startActivity(intent); // Go to blank event screen
            }
        });

        // Assign fields
        recyclerView = view.findViewById(R.id.recyclerView);

        // Create holders
        eventIds = new ArrayList<>();
        eventTitles = new ArrayList<>();
        eventDescs = new ArrayList<>();
        eventStartDates = new ArrayList<>();
        eventEndDates = new ArrayList<>();

        // Update recycler
        updateRecycler(view);


        return view;
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
     * Stores the correct values for the user and all upcoming events in the next 7 day plus all of the events that are currently active
     * @param view view to use for reference
     */
    private void storeDataInArrays(View view){
        // Clear event holders
        eventIds.clear();
        eventTitles.clear();
        eventDescs.clear();
        eventStartDates.clear();
        eventEndDates.clear();

        db = new EventDatabase(view.getContext()); // Get database
        Cursor cursor = db.getAllEventsForLoggedInUser(); // Get result of all events for current user
        if(cursor.getCount() != 0){
            // If data was found loop until all data has been accounted for
            while (cursor.moveToNext()){
                String databaseDateEnd = cursor.getString(5).split("@")[0].strip(); // Get the ending date in the correct format
                String databaseDateEndTime =  cursor.getString(5).split("@")[1].strip(); // Get the ending time of the event
                String databaseDateStart = cursor.getString(4).split("@")[0].strip(); // Get the starting date in the correct format
                String databaseDateStartTime =  cursor.getString(4).split("@")[1].strip(); // Get the starting time of the event
                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mma"); // Date format for dates and time
                try {
                    boolean usernameCorrect = cursor.getString(1).equals(Main.usernameLoggedIn); // Checks that the data return match the expect username
                    Date dEndDate = formatter.parse(databaseDateEnd + " " + databaseDateEndTime); // formats the end date
                    Date dStartDate = formatter.parse(databaseDateStart + " " + databaseDateStartTime); // formats the start date

                    // Set the current date
                    Date sDate = new Date();
                    sDate.setTime(System.currentTimeMillis()); // sets current time to a value
                    sDate = formatter.parse(formatter.format(sDate)); // formats the date correctly

                    // Set the limiter date
                    Date lDate = new Date();
                    lDate.setTime(System.currentTimeMillis() + (7L * 24L * 60L * 60L * 1000L)); // sets limiter time to a value (1 week)
                    lDate = formatter.parse(formatter.format(lDate)); // formats the date correctly

                    // This ensure the event is present as long as it has yet to ended and the start date is within 7 days of the current date
                    boolean beforeEndDate = sDate.before(dEndDate); // Checks if current date is before end date
                    boolean afterTheLimiterDate = lDate.after(dStartDate); // Checks to see if the limiter date is before the start date

                    // Checks If the username and if time is before the end date show as an upcoming event and if the limiter date is after the start date
                    if (usernameCorrect && beforeEndDate && afterTheLimiterDate) {
                        // Assign values to holders
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

            }
        }
    }
}