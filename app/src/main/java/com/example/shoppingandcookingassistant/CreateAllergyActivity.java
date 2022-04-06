package com.example.shoppingandcookingassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateAllergyActivity extends AppCompatActivity {
    // instance variables
    Button submitChoice;
    LinearLayout linLayout;
    CheckBox celery, gluten, crustacean, egg, fish, lupin, milk, mollusc, mustard, nut, peanut, sesameSeed, soybean, sulphite;
    List<CheckBox> checkList;
    TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_equipment);
        linLayout = (LinearLayout) findViewById(R.id.linear_layout);

        desc = findViewById(R.id.description);
        desc.setText("Please select any allergies you have from the list below \nPress 'Continue' when you are done");

        checkList = new ArrayList<CheckBox>();
        getAllAllergies();

        Intent chosenAllergies = new Intent();
        setResult(Activity.RESULT_OK, chosenAllergies);

        // setup continue button
        submitChoice = findViewById(R.id.submit_choice_button);
        submitChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<checkList.size(); i++) {
                    if (checkList.get(i).isChecked()) // any boxes checked will be added to the database
                        submitSelection(String.valueOf(checkList.get(i).getText()), LogInScreenActivity.LOGGED_IN_USER_ID);
                }
                finish();
                // insert code to proceed to the home screen
            }
        });
    }

    public void getAllAllergies() {
        celery = new CheckBox(getApplicationContext());
        celery.setText("Celery");
        checkList.add(celery);

        gluten = new CheckBox(getApplicationContext());
        gluten.setText("Cereals containing gluten");
        checkList.add(gluten);

        crustacean = new CheckBox(getApplicationContext());
        crustacean.setText("Crustaceans");
        checkList.add(crustacean);

        egg = new CheckBox(getApplicationContext());
        egg.setText("Eggs");
        checkList.add(egg);

        fish = new CheckBox(getApplicationContext());
        fish.setText("Fish");
        checkList.add(fish);

        lupin = new CheckBox(getApplicationContext());
        lupin.setText("Lupin");
        checkList.add(lupin);

        milk = new CheckBox(getApplicationContext());
        milk.setText("Milk/lactose");
        checkList.add(milk);

        mollusc = new CheckBox(getApplicationContext());
        mollusc.setText("Molluscs");
        checkList.add(mollusc);

        mustard = new CheckBox(getApplicationContext());
        mustard.setText("Mustard");
        checkList.add(mustard);

        nut = new CheckBox(getApplicationContext());
        nut.setText("Nuts");
        checkList.add(nut);

        peanut = new CheckBox(getApplicationContext());
        peanut.setText("Peanuts");
        checkList.add(peanut);

        sesameSeed = new CheckBox(getApplicationContext());
        sesameSeed.setText("Sesame seeds");
        checkList.add(sesameSeed);

        soybean = new CheckBox(getApplicationContext());
        soybean.setText("Soybeans");
        checkList.add(soybean);

        sulphite = new CheckBox(getApplicationContext());
        sulphite.setText("Sulphites");
        checkList.add(sulphite);

        for(int i=0; i<checkList.size(); i++) {
            linLayout.addView(checkList.get(i));
        }
    }

    public void submitSelection(String allergenName, String userID) {
        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/addAllergy.php";
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
                params.put("userID", userID);
                params.put("allergenName", allergenName);
                return params;
            }
        };
        queue.add(request);
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();


        Intent chosenFilters = new Intent();
        setResult(Activity.RESULT_OK, chosenFilters);
    }
}