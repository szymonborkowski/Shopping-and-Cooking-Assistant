package com.example.shoppingandcookingassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogInScreenActivity extends AppCompatActivity {

    private String passwordFromDB;
    private EditText passwordET;

    public static String LOGGED_IN_USER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        EditText usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        Button loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            // When the button is pressed, if the entries match
            // the placeholder parameters let the user log in.
            @Override
            public void onClick(View v) {
                if(usernameET.getText().toString().equals("u"))
                    finish();

                getPasswordFromDB(usernameET.getText().toString());
            }
        });

        Button signupBtn = findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                Intent intent = new Intent(getApplicationContext(), CreateUserAccountActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        // Empty to prevent user from closing and bypassing the login screen
    }

    public void getPasswordFromDB(String input) {
        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/login.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                System.out.println(response);
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("password0") == null) {
                    Toast.makeText(getApplicationContext(), "Please enter valid username.", Toast.LENGTH_SHORT).show();
                } else {
                    passwordFromDB = jsonObject.getString("password0");

                    if(BCrypt.checkpw(passwordET.getText().toString(), passwordFromDB)) {
                        Toast.makeText(getApplicationContext(), "Successful Login", Toast.LENGTH_SHORT).show();
                        getLoggedInUserID(input);
                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect password/username", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getApplicationContext(), "ERR: " + error, Toast.LENGTH_SHORT).show()) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", input);
                return params;
            }
        };
        queue.add(request);
    }

    public void getLoggedInUserID(String username) {
        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/getUserID.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                System.out.println(response);
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("uID0") == null) {
                    Toast.makeText(getApplicationContext(), "Please enter valid username.", Toast.LENGTH_SHORT).show();
                } else {
                    LOGGED_IN_USER_ID = jsonObject.getString("uID0");
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getApplicationContext(), "ERR: " + error, Toast.LENGTH_SHORT).show()) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                return params;
            }
        };
        queue.add(request);
    }
}