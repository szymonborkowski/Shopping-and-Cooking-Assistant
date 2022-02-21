package com.example.shoppingandcookingassistant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView homeRecommendationFeed;
    HomeRecipeRVAdapter rvAdapter;

    // Temporary sample arrays of names and descriptions:
    String[] recipeNames = {"Baked Feta Pasta"};
    String[] recipeDescriptions = {"Italian oven pasta dish with feta cheese and cherry tomatoes"};
    String[] recipeIngredients = {"• 500g cherry tomatoes\n• 200g feta cheese\n• 300g pasta\n• 3-5 cloves garlic" +
                                  "\n• Olive oil\n• Pinch of salt\n• Crushed red pepper flakes"};
    String[] recipeInstructions = {"1. Preheat oven to around 200°C\n2. In an oven casserole dish combine tomatoes, garlic," +
                                   " crushed red pepper flakes, salt and olive oil.\n" +
                                   "3. Place the feta block in the middle of the dish along with some olive oil on top.\n" +
                                   "4. Bake the dish for 40 min.\n" +
                                   "5. Meanwhile cook the pasta in a pot.\n6. Take out the casserole dish, mash the tomatoes " +
                                   "and feta together, and finally combine the pasta and the sauce together."};

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Get recipe NAMES, DESCRIPTIONS, and IMAGES
        // TODO: Set the String[] arrays to the response
        getHomeFeedFromDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        homeRecommendationFeed = view.findViewById(R.id.homeRV);

        // Accessing database:
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //

        rvAdapter = new HomeRecipeRVAdapter(getActivity(), recipeNames, recipeDescriptions, recipeIngredients, recipeInstructions, homeRecommendationFeed);

        homeRecommendationFeed.setAdapter(rvAdapter);
        homeRecommendationFeed.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void getHomeFeedFromDatabase() {
        // TODO: Fill out with SQL query
    }
}