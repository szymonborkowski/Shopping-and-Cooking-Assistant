package com.example.shoppingandcookingassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class CreateUserAccountActivity extends AppCompatActivity {

    // instance variables
    EditText fName, surname, email, username, password;
    Button continueButton;
    int size;
    public static String SIGNED_UP_USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_account);

        // displaying the GDPR consent form
        Intent intent = new Intent(getApplicationContext(), DataConsentForm.class);
        startActivity(intent);

        fName = findViewById(R.id.first_name_entry);
        surname = findViewById(R.id.surname_entry);
        email = findViewById(R.id.email_entry);
        username = findViewById(R.id.username_entry);
        password = findViewById(R.id.password_entry);
        continueButton = findViewById(R.id.continue_button);

        // adding listener to 'continue' button
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUniqueUsername(username.getText().toString());
            }
        });
    }

    // this method sends the information, provided by the user, to the database to create a new 'userAccount'
    public void createUserInstance(String fName, String surname, String email, String username, String password) {

        // insert BCrypt hashing for password here
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/createUserAccount.php";
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
                Toast.makeText(getApplicationContext(), "Fail to create account" + error, Toast.LENGTH_SHORT).show();
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
                params.put("fname", fName);
                params.put("surname", surname);
                params.put("email", email);
                params.put("username", username);
                params.put("password", hashedPassword);
                return params;
            }
        };
        queue.add(request);
    }

    // this method sends the username, provided by the user, to the database to verify if there are any duplicates
    public void verifyUniqueUsername(String usernameInput) {
        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/getAllUsernames.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // calling a string request method to post the data to the API
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // on below line passing our response to json object.
                    JSONObject jsonObject = new JSONObject(response);
                    size = Integer.valueOf(jsonObject.getString("size"));
                    System.out.println("from inside the method size is: " + size);

                    if(size > 0) {
                        username.getText().clear();
                        Toast.makeText(getApplicationContext(), "Username already taken, please try again", Toast.LENGTH_SHORT).show();
                    } else {
                        createUserInstance(fName.getText().toString(), surname.getText().toString(), email.getText().toString(), username.getText().toString(), password.getText().toString());
                        SIGNED_UP_USER = username.getText().toString();
                        Intent intent = new Intent(getApplicationContext(), CreateEquipmentActivity.class);
                        startActivity(intent);
                        fName.getText().clear();
                        surname.getText().clear();
                        email.getText().clear();
                        password.getText().clear();
                        username.getText().clear();
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() { //This is for error handling of the responses
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fail to create account" + error, Toast.LENGTH_SHORT).show();
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
                params.put("username", usernameInput);
                return params;
            }
        };
        queue.add(request);
    }

    // this method returns the consent form that appears when a user first installs the application.
    public String getWelcomeString() {
        String str = "Welcome to the Shopping And Cooking Assistant application." +
                "\nBefore you begin creating your account, there are a few things we must inform you of" +
                "\n\t\t\t1. As part of creating your own SACA account, you will be asked to provide information that you may deemed sensitive and private. This will include:" +
                "\n\t\t\t-\tNames" +
                "\n\t\t\t-\tEmail Address" +
                "\n\t\t\t-\tFood Allergies" +
                "\n\t\t\t2. To access all features on this application, we will require permission to access your device's camera" +
                "\n\t\t\t3. SACA complies with GDPR standards and all information provided on this application is stored securely and transported over encrypted, secure connections" +
                "\nDo you consent to the above terms and conditions?";
        return str;
    }
}