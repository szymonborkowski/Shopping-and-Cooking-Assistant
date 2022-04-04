package com.example.shoppingandcookingassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class ChooseAllergyActivity extends AppCompatActivity {
    Button submitChoice;
    LinearLayout linLayout;
    CheckBox celery, gluten, crustacean, egg, fish, lupin, milk, mollusc, mustard, nut, peanut, sesameSeed, soybean, sulphite;
    List<CheckBox> checkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergy);
        linLayout = (LinearLayout) findViewById(R.id.linear_layout);

        checkList = new ArrayList<CheckBox>();
        getAllAllergies();


        // method to fetch the user's current equipment settings
        getAllergies("8776");



        Intent chosenAllergies = new Intent();
        setResult(Activity.RESULT_OK, chosenAllergies);

        submitChoice = findViewById(R.id.submit_choice_button_allergy);
        submitChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<checkList.size(); i++) {
                    if (checkList.get(i).isChecked())
                        submitUpdates(String.valueOf(checkList.get(i).getText()), "8776", "addAllergy");
                    else
                        submitUpdates(String.valueOf(checkList.get(i).getText()), "8776", "deleteAllergy");
                }
                finish();
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

    public void getAllergies(String userID) {
        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/getAllergies.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // calling a string request method to post the data to the API
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // on below line passing our response to json object.
                    JSONObject jsonObject = new JSONObject(response);
                    for(int i=0; i<Integer.valueOf(jsonObject.getString("size")); i++) {
                        String str = jsonObject.getString("allergenName" + i);
                        for(int j=0; j<checkList.size(); j++) {
                            if(String.valueOf(checkList.get(j).getText()).equals(str))
                                checkList.get(j).setChecked(true);
                        }
                    }
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
                return params;
            }
        };
        queue.add(request);
    }

    public void submitUpdates(String allergen, String uID, String file) {
        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/" + file + ".php";
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
                params.put("userID", uID);
                params.put("allergenName", allergen);
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

