/**
 * @author Kenneth Lockhart
 * Date: 12/13/2024
 */
package com.example.lockhartkenneth_project.screens;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.lockhartkenneth_project.Main;
import com.example.lockhartkenneth_project.R;
import com.example.lockhartkenneth_project.database.EventDatabase;
import com.example.lockhartkenneth_project.database.LoginDatabase;

public class Settings extends AppCompatActivity {
    private static final int SMS_PERMISSION_CODE = 1 ; // SMS Code
    private static final int POST_PERMISSION_CODE = 2 ; // POST Code

    /**
     * On create prepare fields and listeners
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // assign fields
        Button deleteAccount = findViewById(R.id.deleteButton);
        Button returnHome = findViewById(R.id.returnToHome);
        // Button fields
        Button SMS = findViewById(R.id.smsButton);

        // Delete account listener
        deleteAccount.setOnClickListener(v -> {
            // Get databases and delete user data
            new LoginDatabase(v.getContext()).deleteUser(Main.usernameLoggedIn);
            new EventDatabase(v.getContext()).deleteAllUserEvents(Main.usernameLoggedIn);

            // Log out user
            Toast.makeText(Settings.this, "Logout successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Main.class);
            startActivity(intent);
        });

        // Exit Settings listener
        returnHome.setOnClickListener(v -> {
            // Goto Home screen
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
        });

        // Handles SMS enable messages
        SMS.setOnClickListener(v -> {
            // Check for both the POST and SMS permissions
            if (ContextCompat.checkSelfPermission(Settings.this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            } else {
                // Request POST Permission
                requestPermission(Manifest.permission.POST_NOTIFICATIONS, POST_PERMISSION_CODE);
            }
            if (ContextCompat.checkSelfPermission(Settings.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            } else {
                // Request SMS Permission
                requestPermission(Manifest.permission.SEND_SMS, SMS_PERMISSION_CODE);
            }
        });
    }

    /**
     * Request permission from user
     * @param permission permission to request
     * @param permissionCode permission code
     */
    private void requestPermission(String permission, int permissionCode){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,permission)){
            // Create Dialog box if needed
            new AlertDialog.Builder(this)
                    .setTitle(permission)
                    .setMessage("The application would like to use like access to the following permission.")
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        // Enable the requested permission
                        ActivityCompat.requestPermissions(
                                Settings.this,
                                new String[] {permission},
                                permissionCode);
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        // Dismiss on cancel
                        dialogInterface.dismiss();
                    })
                    .create().show();
        }else{
            // Request User permission
            requestPermissions(
                    new String[] {permission},
                    permissionCode);
        }
    }

    /**
     * Result from the requested permission
     * @param request The request code passed in
     * @param permissions The requested permissions. Never null.
     * @param grant The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int request,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grant) {
        super.onRequestPermissionsResult(request,
                permissions,
                grant);
        // Checks if SMS permission was given. Inform user of result.
        if (request == SMS_PERMISSION_CODE) {
            if (grant.length > 0 && grant[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        // Checks if POST permission was given. Inform user of result.
        if (request == POST_PERMISSION_CODE) {
            if (grant.length > 0 && grant[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
