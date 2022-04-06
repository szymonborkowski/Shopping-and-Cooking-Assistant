package com.example.shoppingandcookingassistant;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SearchFragment extends Fragment {

    ListView listView;

    ArrayList<String> recipeNames;
    ArrayList<String> recipeInstructions;

    /*
     * It's a good practice to define keys for intent extras with your app's package name as a prefix.
     * This ensures that the keys are unique, in case your app interacts with other apps.
     * The key is a public constant because the next activity uses the key to retrieve the text value.
     */
    public static final String RECIPE_NAME = "com.example.shoppingandcookingassistant.RECIPE_NAME";
    public static final String RECIPE_INSTRUCTIONS = "com.example.shoppingandcookingassistant.RECIPE_INSTRUCTIONS";
    public static final String RECIPE_INGREDIENTS = "com.example.shoppingandcookingassistant.RECIPE_INGREDIENTS";

    ArrayAdapter<String> arrayAdapter;

    TextView chosenFiltersTV;

    ArrayList<String> selectedCuisineFilters;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recipeNames = new ArrayList<>();
        recipeInstructions = new ArrayList<>();

        listView = view.findViewById(R.id.recipeListView);  // Displays list of recipes
        arrayAdapter = new ArrayAdapter<>(getActivity(),
                                          R.layout.support_simple_spinner_dropdown_item,
                                          recipeNames);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // open new activity
                openRecipe(view, position);

            }
        });

        // Search:
        SearchView searchView = view.findViewById(R.id.searchRecipeView);
        searchView.setQueryHint("Type here to search");  // Hint for user
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Called when user types in some text and presses 'Enter'
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Insert SQL query here:
                fetchRecipes(query, LogInScreenActivity.LOGGED_IN_USER_ID);
                return false;
            }

            // Called each time a user enters a character
            @Override
            public boolean onQueryTextChange(String newText) {
                // If text bar is empty reset to whole list
                if(newText.equals("")) arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        Button filterButton = view.findViewById(R.id.filtersButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChooseSearchFiltersActivity.class);
                activityResultLauncher.launch(intent);
            }

            ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            Intent intent = result.getData();
                            // retrieve filters?
                            // intent.getStringArrayListExtra()
                            // intent.getBooleanArrayExtra()

                            selectedCuisineFilters = new ArrayList<>();
                            selectedCuisineFilters =
                                    intent.getStringArrayListExtra(ChooseSearchFiltersActivity.CUISINE_FILTERS);

                            String message = "Filters: ";
                            int size = selectedCuisineFilters.size();
                            for(int i = 0; i < size-1; i++) {
                                message += selectedCuisineFilters.get(i) + ", ";
                            }
                            message += selectedCuisineFilters.get(size-1);
                            chosenFiltersTV.setText(message);
                        }
                    }
            );
        });

        chosenFiltersTV = view.findViewById(R.id.chosenFiltersTV);
    }

    public void openRecipe(View view, int pos) {
        // creates an intent based on the passed in class:
        Intent intent = new Intent(getActivity(), DisplayRecipeInstructions.class);

        // pass in values from the chosen recipe:
        intent.putExtra(RECIPE_NAME, arrayAdapter.getItem(pos));

        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/openRecipe.php";
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        //This is for error handling of the responses
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                // on below line passing our response to json object.
                System.out.println(response); //For checking the response if there is an error
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("instructions0") == null) {
                    Toast.makeText(getActivity(), "No instructions for this recipe found.", Toast.LENGTH_SHORT).show();
                } else {

                    int size = Integer.parseInt(jsonObject.getString("size"));
                    String ingredients = "";
                    for(int i = 0; i < size; i++) {
                        ingredients += " • " + jsonObject.getString("name" + i) + " - ";
                        ingredients += jsonObject.getString("amount" + i) + "\n";
                    }

                    String instructions = jsonObject.getString("instructions0");

                    intent.putExtra(RECIPE_INSTRUCTIONS, instructions);
                    intent.putExtra(RECIPE_INGREDIENTS, ingredients);

                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getActivity(), "ERR: " + error, Toast.LENGTH_SHORT).show()) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                params.put("recipeName", arrayAdapter.getItem(pos));
                // at last we are returning our params.
                return params;
            }
        };
        queue.add(request);
    }

    public void fetchRecipes(String query, String userID) {
        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/searchRecipe.php";
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        //This is for error handling of the responses
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                // on below line passing our response to json object.
                System.out.println(response); //For checking the response if there is an error
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("recipeName0") == null) {
                    Toast.makeText(getActivity(), "No recipes of this name found.", Toast.LENGTH_SHORT).show();
                } else {

                    int size = Integer.parseInt(jsonObject.getString("size"));
                    recipeNames.clear();

                    for(int i = 0; i < size; i++) {
                        recipeNames.add(jsonObject.getString("recipeName" + i));
                    }

                    arrayAdapter.getFilter().filter(query);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getActivity(), "ERR: " + error, Toast.LENGTH_SHORT).show()) {
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