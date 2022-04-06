package com.example.shoppingandcookingassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class CreateEquipmentActivity extends AppCompatActivity {
    // instance variables
    Button submitChoice;
    LinearLayout linLayout;
    CheckBox saucepan, bakingDish, fanOven, weighingScales, measuringJug, mixingBowl, grater, fryingPan, strainer, wok, toaster, microwave, whisk;
    List<CheckBox> checkList;
    TextView desc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_equipment);
        linLayout = (LinearLayout) findViewById(R.id.linear_layout);

        desc = findViewById(R.id.description);
        desc.setText("Please select what kitchen equipment you have access to from the list below \nPress 'Continue' when you are done");

        checkList = new ArrayList<CheckBox>();
        getAllEquipment();

        // userID will have to be retrieved from the database once the initial information has been submitted and an instance of 'user' has been created in the database
        getLoggedInUserID(CreateUserAccountActivity.SIGNED_UP_USER);

        Intent chosenEquipment = new Intent();
        setResult(Activity.RESULT_OK, chosenEquipment);

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
                Intent intent = new Intent(getApplicationContext(), CreateAllergyActivity.class);
                startActivity(intent);
            }
        });
    }

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

    public void submitSelection(String equipment, String userID) {
        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/addEquipment.php";
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
                    LogInScreenActivity.LOGGED_IN_USER_ID = jsonObject.getString("uID0");
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