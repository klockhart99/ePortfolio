/**
 * @author Kenneth Lockhart
 * Date: 12/13/2024
 */
package com.example.lockhartkenneth_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lockhartkenneth_project.database.LoginDatabase;
import com.example.lockhartkenneth_project.screens.Home;

public class Main extends AppCompatActivity {

    private LoginDatabase db; // Login database
    private EditText usernameField, passwordField; // Text fields
    private Button signIn, register; // Buttons

    public static String usernameLoggedIn; // Logged in user

    /**
     * On create assign fields and listeners
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameLoggedIn = ""; // On create clears username

        // Log in details assigned
        usernameField = (EditText) findViewById(R.id.textEmail);
        passwordField = (EditText) findViewById(R.id.textPassword);

        // Buttons assigned
        signIn = (Button) findViewById(R.id.loginButton);
        register = (Button) findViewById(R.id.signUpButton);

        db = new LoginDatabase(this); // database accessed

        // Listener for signing in
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user name and password
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                // Checks if the textboxs have data
                if (username.equals("") || password.equals("")) {
                    // If data is missing tell the user they must have all field filled in
                    Toast.makeText(Main.this, "All fields must be filled in.", Toast.LENGTH_SHORT).show();
                }else {
                    // Check if the users log in info is correct
                    Boolean checkPass = db.validateUserLogin(username, password);
                    if (checkPass) {
                        // If true log the user in and take them to the home screen
                        Toast.makeText(Main.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        usernameLoggedIn = username; // Assign logged in user
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);

                    } else {
                        // The username or password was incorrect
                        Toast.makeText(Main.this, "Invalid username or password", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        //This is for the user to be able to register and have their data stored in the database
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get username and password
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                // Checks if the textboxs have data
                if (username.equals("") || password.equals("")) {
                    // If data is missing tell the user they must have all field filled in
                    Toast.makeText(Main.this, "All fields must be filled in.", Toast.LENGTH_SHORT).show();
                }else {
                    // Check if the username is unique
                    Boolean usernameFound = db.checkUsername(username);
                    if (!usernameFound) {
                        // if the username was not found add it to the database and tell the user the account was made
                        db.insertUserInfo(username,password);
                        Toast.makeText(Main.this, "Account creation successful!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Tell the user the account has already been used
                        Toast.makeText(Main.this, "Username has been taken already.", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

    }

}
