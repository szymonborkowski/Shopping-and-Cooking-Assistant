package com.example.shoppingandcookingassistant;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

public class SearchFragment extends Fragment {

    ListView listView;
    String[] recipes = {"Spaghetti", "Vegetable soup", "Curry", "Noodles",
                        "Lasagna", "Burger", "Steak pie", "Fish and chips",
                        "Steak", "Ravioli", "Pasta", "Chicken", "Curry",
                        "Trout", "Salmon", "Potato and egg pie", "Poached Eggs"};

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
}