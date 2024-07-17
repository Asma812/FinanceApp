package com.example.fmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextAddress;
    private DataBaseHelper dataBaseHelper;
    public void openSignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBaseHelper = new DataBaseHelper(this);
        // Initialize EditText fields and Login Button
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextAddress = findViewById(R.id.editTextAddress);
        Button loginButton = findViewById(R.id.login_button); // Remove unnecessary annotations

        // Set OnClickListener for the Login Button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered username, password, and email
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                // Validate login credentials
                if (dataBaseHelper.isValidLogin(username, password)) { // Call isValidLogin from dataBaseHelper
                    // Redirect to homePage activity
                    Intent intent = new Intent(MainActivity.this, homePage.class);
                    startActivity(intent);
                } else {
                    // Display a message indicating invalid credentials
                    Toast.makeText(MainActivity.this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
