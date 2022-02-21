package com.example.shoppingandcookingassistant;

import androidx.appcompat.app.AppCompatActivity;

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


    }

    @Override
    public void onBackPressed() {
        // Empty to prevent user from closing and bypassing the login screen
    }

    public void getPasswordFromDB(String input) {
        String url = "http://(ip address here)/saca_network/login.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() { //@@@
            @Override
            public void onResponse(String response) { //@@@
                try {
                    // on below line passing our response to json object.
                    System.out.println(response); //For checking the response if there is an error
                    JSONObject jsonObject = new JSONObject(response); //@@@
                    if (jsonObject.getString("password") == null) {
                        Toast.makeText(getApplicationContext(), "Please enter valid username.", Toast.LENGTH_SHORT).show();
                    } else {
                        /*
                         * This is where the sql results will be handled/displayed.
                         * The code for the application will be different here, depending on the design of the app.
                         * use jsonObject.getString(String columnName) to return a string object of the desired results
                         */

                        // username is a varchar(50)
                        // password is a varchar(50)

                        passwordFromDB = jsonObject.getString("password");

                        if(BCrypt.checkpw(passwordET.getText().toString(), passwordFromDB)) {
                            Toast.makeText(getApplicationContext(), "Successful Login", Toast.LENGTH_SHORT).show();
                            finish();  // close the activity
                        } else {
                            Toast.makeText(getApplicationContext(), "Incorrect password/username", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() { //This is for error handling of the responses
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERR: " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                /*
                 * Below are all of the input parameters for the php file.
                 * In this example it is the user id.
                 * if we were trying to fetch the user id from username we would write:
                 *      params.put("username", input);
                 */
                params.put("username", input);
                // at last we are returning our params.
                return params;
            }
        };
        queue.add(request);
    }
}