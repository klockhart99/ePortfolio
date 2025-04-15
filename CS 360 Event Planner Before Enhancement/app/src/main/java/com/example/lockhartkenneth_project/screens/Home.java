/**
 * @author Kenneth Lockhart
 * Date: 12/13/2024
 */
package com.example.lockhartkenneth_project.screens;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.example.lockhartkenneth_project.Main;
import com.example.lockhartkenneth_project.R;
import com.example.lockhartkenneth_project.fragments.Calander;
import com.example.lockhartkenneth_project.fragments.UpcomingEvents;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

/**
 * The home screen of the application
 */
public class Home extends AppCompatActivity {
    private NavigationView sideMenu; // Side menu options
    private FragmentManager fragmentManager; // fragment manager
    private FragmentContainerView fragmentContainerView; // fragment container
    private BottomNavigationView bottomMenu; // bottom menu options

    private ImageButton menuButton; // menu button for side menu

    /**
     * Assign fields and create home view
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assign fields and defaults
        sideMenu = findViewById(R.id.sideMenu);
        sideMenu.setVisibility(View.INVISIBLE);
        menuButton = findViewById(R.id.menuButton);
        fragmentContainerView = findViewById(R.id.containerView);
        fragmentManager = fragmentContainerView.getFragment().getParentFragmentManager();
        bottomMenu = findViewById(R.id.bottomMenuBar);

        createNotificationChannel();

        // setup the bottom bar
        setupBottomMenuActions();

        // menubutton listener for on click reveal the side menu
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sideMenu.setVisibility(View.VISIBLE);
            }
        });

        // side menu navigation listener
        sideMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // If any button other then settings or log out is click return to invisible
                if (item.getItemId() == R.id.nav_settings){
                    // If settings is clicked go to settings screen
                    Intent intent = new Intent(getApplicationContext(), Settings.class);
                    startActivity(intent);
                }else if (item.getItemId() == R.id.nav_log_out){
                    // If logout is clicked go to the log in screen and clear the user logged in
                    Toast.makeText(Home.this, "Logout successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Main.class);
                    startActivity(intent);
                }
                // Always hide the menu after an action
                sideMenu.setVisibility(View.INVISIBLE);
                return true;
            }
        });


    }

    /**
     * Holds the bottom menu listener for dividing up the two menus
     */
    private void setupBottomMenuActions() {
        bottomMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bottomEvents){
                    // If the events button is clicked switch fragments to the upcoming events fragment
                    fragmentManager.beginTransaction().replace(R.id.containerView,UpcomingEvents.class, null).commit();
                }else if (item.getItemId() == R.id.bottomCalander){
                    // If the calander button is clicked switch fragments to the calander events fragment
                    fragmentManager.beginTransaction().replace(R.id.containerView,Calander.class, null).commit();
                }
                return true;
            }
        });
    }

    /**
     * Creates a channel called reminders for SMS.
     */
    private void createNotificationChannel() {
        CharSequence name = "Reminder"; // Name reminder
        String desc = "For Reminder"; // What the reminder is for
        int imp = NotificationManager.IMPORTANCE_DEFAULT; // Importance
        NotificationChannel ch = new NotificationChannel("notifyText", name, imp); // assign channel
        ch.setDescription(desc); // set channel description

        // Add channel to notification manager
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(ch);
    }

}
