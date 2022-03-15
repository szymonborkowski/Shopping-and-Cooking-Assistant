package com.example.shoppingandcookingassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddRecipeEvent extends AppCompatActivity {

    Button saveRecipe;

    TextView recipeName;
    TextView lastDate;
    TextView totalPortions;
    Button changeRecipeBtn;
    String chosenRecipeInstructions;
    String ingredients;
    ArrayList<String> ingredientListName;
    ArrayList<String> ingredientListAmount;

    CalendarHorizontalNumberSelector daysSelector;
    PortionHorizontalNumberSelector portionsSelector;

    public static final String CHOSEN_RECIPE = "com.example.calendarexperiment.CHOSEN_RECIPE";
    public static final String PORTIONS = "com.example.calendarexperiment.PORTIONS";
    public static final String DAYS_LEFT = "com.example.calendarexperiment.DAYS_LEFT";
    public static final String CHOSEN_RECIPE_INGREDIENT_NAMES = "com.example.calendarexperiment.CHOSEN_RECIPE_INGREDIENT_NAMES";
    public static final String CHOSEN_RECIPE_INGREDIENT_AMOUNTS = "com.example.calendarexperiment.CHOSEN_RECIPE_INGREDIENT_AMOUNTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_event);

        Intent intent = getIntent();
        String date = intent.getStringExtra(CalendarFragment.CHOSEN_DATE);

        saveRecipe = findViewById(R.id.saveRecipeEventBtn);

        saveRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send over a prompt to add a recipe to a list
                Intent addRecipe = new Intent();
                addRecipe.putExtra(CHOSEN_RECIPE, recipeName.getText().toString());
                addRecipe.putExtra(PORTIONS, totalPortions.getText());
                addRecipe.putExtra(DAYS_LEFT, String.valueOf(daysSelector.getNumber()));
                addRecipe.putStringArrayListExtra(CHOSEN_RECIPE_INGREDIENT_NAMES, ingredientListName);
                addRecipe.putStringArrayListExtra(CHOSEN_RECIPE_INGREDIENT_AMOUNTS, ingredientListAmount);
                setResult(Activity.RESULT_OK, addRecipe);
                finish();
            }
        });

        recipeName = findViewById(R.id.chosenRecipeTV);

        lastDate = findViewById(R.id.lastDateTV);
        totalPortions = findViewById(R.id.portionsTV);

        daysSelector = findViewById(R.id.daysSelector);

        portionsSelector = findViewById(R.id.peopleSelector);
        portionsSelector.setTV(totalPortions, daysSelector);

        daysSelector.setTVAndDate(lastDate, date, portionsSelector);

        // move into calhozselector?
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        date = dateFormat.format(c.getTime());

        lastDate.setText(date);

        String message = "Total portions: 1";
        totalPortions.setText(message);

        changeRecipeBtn = findViewById(R.id.changeRecipeBtn);
        changeRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchRecipeActivity.class);
                activityResultLauncher.launch(intent);

                // on result get chosen recipe

                // TODO: ON RESULT METHOD SET TV TO XYZ
            }
            ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            Intent intent = result.getData();
                            fetchIngredients(intent.getStringExtra(SearchRecipeActivity.SELECTED_RECIPE_ID));
                            String selectedRecipe = intent.getStringExtra(SearchRecipeActivity.SELECTED_RECIPE_BY_USER);
                            recipeName.setText(selectedRecipe);
                        }
                    }
            );
        });
    }

    public void fetchIngredients(String recipeID) {
        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/getIngredientsForRecipe.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        //This is for error handling of the responses
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                // on below line passing our response to json object.
                System.out.println(response); //For checking the response if there is an error
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("name0") == null) {
                    Toast.makeText(getApplicationContext(), "No recipes of this name found.", Toast.LENGTH_SHORT).show();
                } else {
                    int size = Integer.parseInt(jsonObject.getString("size"));
                    ingredientListName = new ArrayList<>();
                    ingredientListAmount = new ArrayList<>();
                    for(int i = 0; i < size; i++) {
                        ingredientListName.add(jsonObject.getString("name" + i));
                        ingredientListAmount.add(jsonObject.getString("amount" + i));
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
                // below line we are creating a map for storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                params.put("recipeID", recipeID);
                // at last we are returning our params.
                return params;
            }
        };
        queue.add(request);
    }
}