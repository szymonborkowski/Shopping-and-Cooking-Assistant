package com.example.shoppingandcookingassistant;

/**
 * This is the ChooseEquipmentActivity class.
 * This class is an activity that is created when the corresponding CardView is clicked in the user profile fragment.
 * Note: The code for this class is adapted for the ChooseAllergyActivity class
 * @author Anthony Bird
 */

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

public class ChooseEquipmentActivity extends AppCompatActivity {
    // instance variables
    Button submitChoice;
    LinearLayout linLayout;
    CheckBox saucepan, bakingDish, fanOven, weighingScales, measuringJug, mixingBowl, grater, fryingPan, strainer, wok, toaster, microwave, whisk;
    List<CheckBox> checkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        linLayout = (LinearLayout) findViewById(R.id.linear_layout);

        // checkList holds all of the CheckBox objects
        checkList = new ArrayList<CheckBox>();
        getAllEquipment();

        // method to fetch the user's current equipment settings
        getEquipment("8776");

        Intent chosenEquipment = new Intent();
        setResult(Activity.RESULT_OK, chosenEquipment);

        // setup submit button
        submitChoice = findViewById(R.id.submit_choice_button);
        submitChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<checkList.size(); i++) {
                    if (checkList.get(i).isChecked()) // any boxes checked will be added to the database
                        submitUpdates(String.valueOf(checkList.get(i).getText()), "8776", "addEquipment");
                    else // any boxes unchecked will be deleted from the database
                        submitUpdates(String.valueOf(checkList.get(i).getText()), "8776", "deleteEquipment");
                }
                finish();
            }
        });
    }

    /**
     * This method is used to populate the Checkboxes with all of the equipment.
     * Note: this operation does not interact with the database because it causes latency issues when querying what equipment the user has.
     */
    public void getAllEquipment() {
        saucepan = new CheckBox(getApplicationContext());
        saucepan.setText("Saucepan");
        checkList.add(saucepan);

        bakingDish = new CheckBox(getApplicationContext());
        bakingDish.setText("Baking Dish");
        checkList.add(bakingDish);

        fanOven = new CheckBox(getApplicationContext());
        fanOven.setText("Fan Oven");
        checkList.add(fanOven);

        weighingScales = new CheckBox(getApplicationContext());
        weighingScales.setText("Weighing Scales");
        checkList.add(weighingScales);

        measuringJug = new CheckBox(getApplicationContext());
        measuringJug.setText("Measuring Jug");
        checkList.add(measuringJug);

        mixingBowl = new CheckBox(getApplicationContext());
        mixingBowl.setText("Mixing Bowl");
        checkList.add(mixingBowl);

        grater = new CheckBox(getApplicationContext());
        grater.setText("Grater");
        checkList.add(grater);

        fryingPan = new CheckBox(getApplicationContext());
        fryingPan.setText("Frying Pan");
        checkList.add(fryingPan);

        strainer = new CheckBox(getApplicationContext());
        strainer.setText("Strainer");
        checkList.add(strainer);

        wok = new CheckBox(getApplicationContext());
        wok.setText("Wok");
        checkList.add(wok);

        toaster = new CheckBox(getApplicationContext());
        toaster.setText("Toaster");
        checkList.add(toaster);

        microwave = new CheckBox(getApplicationContext());
        microwave.setText("Microwave");
        checkList.add(microwave);

        whisk = new CheckBox(getApplicationContext());
        whisk.setText("Whisk");
        checkList.add(whisk);

        // add each CheckBox object to the Linear Layout
        for(int i=0; i<checkList.size(); i++) {
            linLayout.addView(checkList.get(i));
        }
    }

    /**
     * This method is used to obtain the equipment that the user has access to (i.e: has a relationship with in the database).
     * @param userID
     */
    public void getEquipment(String userID) {
        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/getEquipment.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // calling a string request method to post the data to the API
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // on below line passing our response to json object.
                    JSONObject jsonObject = new JSONObject(response);
                    for(int i=0; i<Integer.valueOf(jsonObject.getString("size")); i++) {
                        String str = jsonObject.getString("toolName" + i);
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

    public void submitUpdates(String equipment, String uID, String file) {
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
                params.put("toolName", equipment);
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
