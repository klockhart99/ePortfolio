/**
 * @author Kenneth Lockhart
 * Date: 12/13/2024
 */
package com.example.lockhartkenneth_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lockhartkenneth_project.database.EventDatabase;
import com.example.lockhartkenneth_project.fragments.Calander;
import com.example.lockhartkenneth_project.fragments.UpcomingEvents;
import com.example.lockhartkenneth_project.screens.Event;

import java.util.ArrayList;

/**
 * Custom Adaptor for the Recycle viewer
 */
public class CustomAdaptor extends RecyclerView.Adapter<CustomAdaptor.CustomViewHolder>{
    private final Fragment fragment; // Fragment that holds the recycler viewer
    private final Context context; // context
    private final ArrayList<String> eventIds;
    private final ArrayList<String> evenntTitles;
    private final ArrayList<String> eventsDescs;
    private final ArrayList<String> eventsStartDates;


    public CustomAdaptor(Fragment fragment, Context context,
                         ArrayList<String> eventId,
                         ArrayList<String> eventTitle,
                         ArrayList<String> eventDesc,
                         ArrayList<String> eventStartDate){
        // Assign Fields from constructor
        this.fragment = fragment;
        this.context = context;
        this.eventIds = eventId;
        this.evenntTitles = eventTitle;
        this.eventsDescs = eventDesc;
        this.eventsStartDates = eventStartDate;

    }

    /**
     * Create the view for view holder
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return the custom view holder
     */
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_example, parent, false);
        return new CustomViewHolder(view);
    }

    /**
     * Bind the view holder
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param pos The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int pos) {

        // Set holder text fields
        holder.title.setText(String.valueOf(evenntTitles.get(pos)));
        holder.description.setText(String.valueOf(eventsDescs.get(pos)));
        holder.startDate.setText(String.valueOf(eventsStartDates.get(pos)));

        // Set holder buttons and listeners
        holder.itemView.findViewById(R.id.editEventButton).setOnClickListener((view -> {
            // On edit send row data to Event and switch to event screen
            Intent intent = new Intent(context, Event.class);
            Event.rowId = Integer.valueOf(String.valueOf(eventIds.get(pos)));
            fragment.startActivity(intent);
        }));
        holder.itemView.findViewById(R.id.deleteEventButton).setOnClickListener((view -> {
            // On delete remove the row data and refresh the recycler
            EventDatabase db = new EventDatabase(view.getContext());

            String rowId = (String.valueOf(eventIds.get(pos))); // row id
            db.deleteSingleRowEvent(rowId); // Delete single row

            // Get the instance of the fragment to update recycler
            if (fragment instanceof UpcomingEvents){
                UpcomingEvents events = (UpcomingEvents) fragment;
                events.updateRecycler(view);
            }
            if (fragment instanceof Calander){
                Calander calander = (Calander) fragment;
                calander.updateRecycler(view);
            }
        }));

    }

    /**
     * get the number of events
     * @return number of events
     */
    @Override
    public int getItemCount() {
        return eventIds.size();
    }

    /**
     * Custom view holder for events
     */
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView title, description, startDate; // Textviews of events

        public CustomViewHolder(@NonNull View itemV) {
            super(itemV);
            // assign fields
            title = itemView.findViewById(R.id.eventNameList);
            description = itemView.findViewById(R.id.eventDescList);
            startDate = itemView.findViewById(R.id.eventStartTime);
        }
    }

}
