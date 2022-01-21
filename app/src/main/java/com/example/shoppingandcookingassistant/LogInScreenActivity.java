package com.example.shoppingandcookingassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogInScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);

        EditText usernameET = findViewById(R.id.usernameET);
        EditText passwordET = findViewById(R.id.passwordET);
        Button loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            // When the button is pressed, if the entries match
            // the placeholder parameters let the user log in.
            @Override
            public void onClick(View v) {
                if(usernameET.getText().toString().equals("user")
                && passwordET.getText().toString().equals("pass")) {
                    finish();  // close the activity
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Empty to prevent user from closing the login screen
    }
}