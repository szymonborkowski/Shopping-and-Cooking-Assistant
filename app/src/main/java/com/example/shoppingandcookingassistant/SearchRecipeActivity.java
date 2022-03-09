package com.example.shoppingandcookingassistant;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchRecipeActivity extends AppCompatActivity {

    ListView listOfRecipes;
    SearchView searchViewRecipes;
    ArrayAdapter<String> arrayAdapter;
    Button filtersBtn;
    TextView selectedFilters;
    ArrayList<String> recipeNames;
    ArrayList<String> recipeInstructions;
    ArrayList<String> selectedCuisineFilters;
    AdapterView.OnItemClickListener userSelectsRecipeListener;
    public static final String SELECTED_RECIPE_BY_USER = "com.example.shoppingandcookingassistant.SELECTED_RECIPE_BY_USER";
    public static final String SELECTED_RECIPE_INGREDIENTS = "com.example.shoppingandcookingassistant.SELECTED_RECIPE_INGREDIENTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);

        // *** SEARCH VIEW ***
        searchViewRecipes = findViewById(R.id.searchViewRecipes);
        searchViewRecipes.setQueryHint("Type here to search");
        searchViewRecipes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                fetchRecipes(s, LogInScreenActivity.LOGGED_IN_USER_ID);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")) arrayAdapter.getFilter().filter(s);
                return false;
            }
        });

        // *** ARRAY ADAPTER ***
        recipeNames = new ArrayList<>();
        recipeInstructions = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(getApplication(),
                                          R.layout.support_simple_spinner_dropdown_item,
                                          recipeNames);

        // *** LIST OF RECIPES LISTENER ***
        userSelectsRecipeListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent chosenRecipe = new Intent();
                String recipeName = (String)adapterView.getItemAtPosition(i);
                String recipeInstruction = recipeInstructions.get(recipeNames.indexOf(recipeName));
                chosenRecipe.putExtra(SELECTED_RECIPE_BY_USER, recipeName);
                chosenRecipe.putExtra(SELECTED_RECIPE_INGREDIENTS, recipeInstruction);
                setResult(Activity.RESULT_OK, chosenRecipe);

                finish();
            }
        };

        // *** LIST OF RECIPES ***
        listOfRecipes = findViewById(R.id.listViewRecipes);
        listOfRecipes.setAdapter(arrayAdapter);
        listOfRecipes.setOnItemClickListener(userSelectsRecipeListener);

        // *** FILTER BUTTON ***
        filtersBtn = findViewById(R.id.filtersBtn);
        filtersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChooseSearchFiltersActivity.class);
                activityResultLauncher.launch(intent);
            }
            ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            Intent intent = result.getData();

                            selectedCuisineFilters = new ArrayList<>();
                            selectedCuisineFilters =
                                    intent.getStringArrayListExtra(ChooseSearchFiltersActivity.CUISINE_FILTERS);

                            String message = "Filters: ";
                            for(String filter : selectedCuisineFilters) {
                                message += filter + ", ";
                            }

                            selectedFilters.setText(message);
                        }
                    }
            );
        });

        // *** FILTER TEXTVIEW ***
        selectedFilters = findViewById(R.id.selectedFilters);
    }

    public void fetchRecipes(String query, String userID) {
        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/queryRecipe.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        //This is for error handling of the responses
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                // on below line passing our response to json object.
                System.out.println(response); //For checking the response if there is an error
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("recipeName0") == null) {
                    Toast.makeText(getApplicationContext(), "No recipes of this name found.", Toast.LENGTH_SHORT).show();
                } else {

                    int size = Integer.parseInt(jsonObject.getString("size"));
                    recipeNames.clear();
                    recipeInstructions.clear();

                    for(int i = 0; i < size; i++) {
                        recipeNames.add(jsonObject.getString("recipeName" + i));
                        recipeInstructions.add(jsonObject.getString("instructions" + i));
                    }

                    arrayAdapter.getFilter().filter(query);
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
                params.put("uID", userID);
                // at last we are returning our params.
                return params;
            }
        };
        queue.add(request);
    }


}