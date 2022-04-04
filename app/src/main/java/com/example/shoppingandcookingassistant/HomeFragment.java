package com.example.shoppingandcookingassistant;

import android.content.Intent;
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
import android.widget.ImageView;
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

public class HomeFragment extends Fragment {

    RecyclerView rvResults;
    ResultsAdapter rAdapter;
    //HomeRecipeRVAdapter rvAdapter;
    List<Results.ResultsHandler> rhList;

    ArrayList<String> idList, nameList, descList, cuisineList, mealList;

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
        getData("8776", "loadHomeFragment");

        // set up the RecyclerView to display the results
        rvResults = view.findViewById(R.id.rvResults);
        rhList = new ArrayList<>();
        rAdapter = new ResultsAdapter(getActivity(), rhList, rvResults, nameList);
        rvResults.setAdapter(rAdapter);
        rvResults.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    /**
     * This is the method for communicating with the database.
     * This will be used for:
     *      - fetching a user's id given their username
     *      - fetching 'available' recipes given that user's id number
     *
     * Note: for SQL operations that require 'INSERT' or 'UPDATE' i.e: multiple input parameters, additional parameters need to be included into this method.
     *
     * 'available' refers to recipes that the user is not allergic to, have the correct kitchen equipment and ingredients
     *
     * @param input - the input parameter for the HTTP POST Request
     * @param operation - The name of the php file to be executed
     */
    private void getData(String input, String operation) {
        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/" + operation + ".php";
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // calling a string request method to post the data to the API
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // on below line passing our response to json object.
                    JSONObject jsonObject = new JSONObject(response);
                    if (Integer.valueOf(jsonObject.getString("size")) == 0) { // if the executed query returned no results
                        Toast.makeText(getActivity(), "Query returned no results", Toast.LENGTH_SHORT).show();
                    } else {
                        // use jsonObject.getString(String columnName) to return a string object of the desired result
                        // below line will return the number of recipes that have been found
                        int size = Integer.valueOf(jsonObject.getString("size"));

                        // recipe attributes (id, name, description, cuisine, meal) will be stored in individual lists
                        idList = new ArrayList<>();
                        nameList = new ArrayList<>();
                        descList = new ArrayList<>();
                        cuisineList = new ArrayList<>();
                        mealList = new ArrayList<>();

                        // populate each attribute list with the recipe results
                        for (int i=0; i<size; i++) {
                            idList.add(jsonObject.getString("rID" + i));
                            nameList.add(jsonObject.getString("recipeName" + i));
                            descList.add(jsonObject.getString("description" + i));
                            cuisineList.add(jsonObject.getString("cuisine" + i));
                            mealList.add(jsonObject.getString("meal" + i));
                        }

                        // images corresponding to each recipe will be downloaded and displayed in separate threads
                        for(int i=0; i<size; i++) {
                            Results res = new Results(idList.get(i), nameList.get(i), descList.get(i), cuisineList.get(i), mealList.get(i), rhList);
                            res.start();
                            res.join();
                            // notify the RecycleView adapter of changes to its parameter list
                            rAdapter.notifyItemInserted(i+1);
                        }
                    }
                } catch (JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() { //This is for error handling of the responses
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Fail to get recipe" + error, Toast.LENGTH_SHORT).show();
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
                params.put("uID", input);
                return params;
            }
        };
        queue.add(request);
    }
}