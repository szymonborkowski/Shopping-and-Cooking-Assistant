package com.example.shoppingandcookingassistant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {

    RecyclerView homeRecommendationFeed;
    HomeRecipeRVAdapter rvAdapter;

    // Temporary sample arrays of names and descriptions:
    String[] recipeNames = {"Spaghetti", "Vegetable soup", "Curry", "Noodles",
                            "Lasagna", "Burger", "Steak pie", "Fish and chips"};
    String[] recipeDescriptions = {"Spaghetti instructions", "Vegetable soup instructions",
                                   "Curry instructions", "Noodles instructions", "Lasagna instructions",
                                   "Burger instructions", "Steak pie instructions", "Fish and chips instructions"};

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        rvAdapter = new HomeRecipeRVAdapter(getActivity(), recipeNames, recipeDescriptions, homeRecommendationFeed);

        homeRecommendationFeed.setAdapter(rvAdapter);
        homeRecommendationFeed.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}