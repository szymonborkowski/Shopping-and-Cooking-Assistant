package com.example.shoppingandcookingassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class UserDetailsActivity extends AppCompatActivity {
    // instance variables
    RecyclerView recyclerView;
    UserDetailsRVAdapter rvAdapter;
    Button updateDetails;
    String[] headings, userData;
    TextView equipmentTV, allergyTV;
    CardView equipmentHolder, allergyHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // set the headings to be displayed in TexViews
        headings = new String[] {"First Name", "Last Name", "Email", "Username"};

        // get user details from the database for the user
        getUserDetails(headings, LogInScreenActivity.LOGGED_IN_USER_ID);

        recyclerView = findViewById(R.id.recyclerView);

        // set listener for equipment button to display list of equipment checkboxes
        equipmentTV = findViewById(R.id.equipment_entry);
        equipmentHolder = findViewById(R.id.equipment_holder);
        equipmentHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseEquipmentActivity.class);
                startActivity(intent);
            }
        });

        // set listener for allergy button to display list of allergy checkboxes
        allergyTV = findViewById(R.id.allergy_entry);
        allergyHolder = findViewById(R.id.allergy_holder);
        allergyHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseAllergyActivity.class);
                startActivity(intent);
            }
        });

        // assign 'confirm' button and set listener
        updateDetails = findViewById(R.id.confirm_button);
        updateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send new user details to the database for user
                confirmDetails(userData, LogInScreenActivity.LOGGED_IN_USER_ID);
                Toast.makeText(getApplicationContext(), "Details updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method is called when the user presses the 'confirm' button for this activity.
     * This method updates the user's data in the database.
     * @param userData - The data entered by the user
     * @param uID - The users unique ID number
     */
    public void confirmDetails(String[] userData, String uID) {
        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/updateUserDetails.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // calling a string request method to post the data to the API
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // on below line passing our response to json object.
                    JSONObject jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() { //This is for error handling of the responses
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fail to submit new details" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                // creating a map for storing values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // input parameter(s) for the HTTP POST request
                params.put("uID", uID);
                params.put("fname", userData[0]);
                params.put("surname", userData[1]);
                params.put("email", userData[2]);
                params.put("username", userData[3]);
                return params;
            }
        };
        queue.add(request);
    }

    /**
     * This method obtains the user's details from the database, given their userID.
     * This method also implements the RecyclerView adapter to show the details on the application
     * @param headings - The headings describing each of the user details
     * @param uID - The unique ID number of the user
     */
    public void getUserDetails(String[] headings, String uID) {
        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/getUserDetails.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // calling a string request method to post the data to the API
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // on below line passing our response to json object.
                    JSONObject jsonObject = new JSONObject(response);
                    // use jsonObject.getString(String columnName) to return a string object of the desired result
                    userData = new String[]{jsonObject.getString("fname"), jsonObject.getString("surname"), jsonObject.getString("email"), jsonObject.getString("username")};

                    // setting up adapter for RecyclerView
                    rvAdapter = new UserDetailsRVAdapter(getApplicationContext(), headings, userData);
                    recyclerView.setAdapter(rvAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() { //This is for error handling of the responses
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fail to get user details" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                // creating a map for storing values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // input parameter(s) for the HTTP POST request
                params.put("uID", uID);
                return params;
            }
        };
        queue.add(request);
    }
}