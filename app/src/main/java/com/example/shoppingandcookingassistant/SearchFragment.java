package com.example.shoppingandcookingassistant;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchFragment extends Fragment {

    ListView listView;

    // Sample recipes:
    String[] recipes = {"Spaghetti", "Vegetable soup", "Curry", "Noodles",
                        "Lasagna", "Burger", "Steak pie", "Fish and chips",
                        "Steak", "Ravioli", "Pasta", "Chicken", "Curry",
                        "Trout", "Salmon", "Potato and egg pie", "Poached Eggs"};

    // Sample instructions:
    String[] instructions = {"Spaghetti instructions", "Vegetable soup instructions",
            "Curry instructions", "Noodles instructions", "Lasagna instructions",
            "Burger instructions", "Steak pie instructions", "Fish and chips instructions",
            "Steak instructions", "Ravioli instructions", "Pasta instructions",
            "Chicken instructions", "Curry instructions",
            "Trout instructions", "Salmon instructions", "Potato and egg pie instructions",
            "Poached Eggs instructions"};

    /*
     * It's a good practice to define keys for intent extras with your app's package name as a prefix.
     * This ensures that the keys are unique, in case your app interacts with other apps.
     * The key is a public constant because the next activity uses the key to retrieve the text value.
     */
    public static final String RECIPE_NAME = "com.example.shoppingandcookingassistant.RECIPE_NAME";
    public static final String RECIPE_INSTRUCTIONS = "com.example.shoppingandcookingassistant.RECIPE_INSTRUCTIONS";

    ArrayAdapter<String> arrayAdapter;

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

        listView = view.findViewById(R.id.listView);  // Displays list of recipes
        arrayAdapter = new ArrayAdapter<>(getActivity(),
                                          R.layout.support_simple_spinner_dropdown_item,
                                          recipes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // open new activity
                openRecipe(view, position);

            }
        });

        SearchView searchView = view.findViewById(R.id.searchRecipeView);
        searchView.setQueryHint("Type here to search");  // Hint for user

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Called when user types in some text and presses 'Enter'
            @Override
            public boolean onQueryTextSubmit(String query) {
                // results get displayed
                arrayAdapter.getFilter().filter(query);
                return false;
            }

            // Called each time a user enters a character
            @Override
            public boolean onQueryTextChange(String newText) {
                // If text bar is empty reset to whole list
                if(newText.equals(""))
                    arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void openRecipe(View view, int pos) {
        // creates an intent based on the passed in class:
        Intent intent = new Intent(getActivity(), DisplayRecipeInstructions.class);

        // pass in values from the chosen recipe:
        intent.putExtra(RECIPE_NAME, recipes[pos]);
        intent.putExtra(RECIPE_INSTRUCTIONS, instructions[pos]);

        startActivity(intent);
    }
}