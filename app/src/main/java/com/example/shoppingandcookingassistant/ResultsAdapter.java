package com.example.shoppingandcookingassistant;

/**
 * This is the ResultsAdapter class, defining nested class ViewHolder.
 * Both classes here are used to map each query result to the RecyclerView.
 * @author Anthony Bird
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {
    // instance variables
    private List<Results.ResultsHandler> rhList;
    Context context;
    RecyclerView recyclerView;
    List<String> nameList;

    public static final String RECIPE_NAME = "com.example.shoppingandcookingassistant.RECIPE_NAME";
    public static final String RECIPE_INSTRUCTIONS = "com.example.shoppingandcookingassistant.RECIPE_INSTRUCTIONS";
    public static final String RECIPE_INGREDIENTS = "com.example.shoppingandcookingassistant.RECIPE_INGREDIENTS";


    public ResultsAdapter(Context context, List<Results.ResultsHandler> rhList, RecyclerView recyclerView, ArrayList<String> nameList) {
        this.context = context;
        this.rhList = rhList;
        this.recyclerView = recyclerView;
        this.nameList = nameList;
    }

    @NonNull
    @Override
    public ResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View resultsView = inflater.inflate(R.layout.populate_results, parent, false);
        ViewHolder viewHolder = new ViewHolder(resultsView);

        // TODO: Move the open recipe code from the HomeFragment to here...
        resultsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = recyclerView.getChildAdapterPosition(view);
                String recipeName = rhList.get(itemPosition).getRecipeName();
                getRecipeInstructionsIngredients(recipeName);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ResultsAdapter.ViewHolder holder, int position) {
        Results.ResultsHandler resultsHandler = rhList.get(position);

        // mapping the data to a view
        TextView recipeNameTV = holder.recipeNameTV;
        recipeNameTV.setText(resultsHandler.getRecipeName());

        TextView descTV = holder.descTV;
        descTV.setText(resultsHandler.getDescription());

        TextView cuisineTV = holder.cuisineTV;
        cuisineTV.setText(resultsHandler.getCuisine());

        TextView mealTV = holder.mealTV;
        mealTV.setText(resultsHandler.getMeal());

        ImageView recipeImageTV = holder.recipeImageIV;
        recipeImageTV.setImageBitmap(resultsHandler.getBitIn());
    }

    @Override
    public int getItemCount() {
        return rhList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // instance variables
        public TextView recipeNameTV, descTV, cuisineTV, mealTV;
        public ImageView recipeImageIV;
        public CardView resHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            // the data for each recipe will be displayed in a CardView
            resHolder = (CardView) itemView.findViewById(R.id.res_holder);
            recipeNameTV = (TextView) itemView.findViewById(R.id.recipe_name);
            descTV = (TextView) itemView.findViewById(R.id.recipe_description);
            cuisineTV = (TextView) itemView.findViewById(R.id.recipe_cuisine);
            mealTV = (TextView) itemView.findViewById(R.id.recipe_meal);
            recipeImageIV = (ImageView) itemView.findViewById(R.id.recipe_image);
        }
    }

    public void getRecipeInstructionsIngredients(String recipeName) {
        // creates an intent based on the passed in class:
        Intent intent = new Intent(context, DisplayRecipeInstructions.class);

        // pass in values from the chosen recipe:
        intent.putExtra(RECIPE_NAME, recipeName);

        String url = "https://easyshoppingeasycooking.eu.ngrok.io/saca_network/openRecipe.php";
        RequestQueue queue = Volley.newRequestQueue(context);

        //This is for error handling of the responses
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                // on below line passing our response to json object.
                System.out.println(response); //For checking the response if there is an error
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("instructions0") == null) {
                    Toast.makeText(context, "No instructions for this recipe found.", Toast.LENGTH_SHORT).show();
                } else {

                    int size = Integer.parseInt(jsonObject.getString("size"));
                    String ingredients = "";
                    for(int i = 0; i < size; i++) {
                        ingredients += jsonObject.getString("name" + i) + " ";
                        ingredients += jsonObject.getString("amount" + i) + "\n";
                    }

                    String instructions = jsonObject.getString("instructions0");

                    intent.putExtra(RECIPE_INSTRUCTIONS, instructions);
                    intent.putExtra(RECIPE_INGREDIENTS, ingredients);

                    context.startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(context, "ERR: " + error, Toast.LENGTH_SHORT).show()) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                params.put("recipeName", recipeName);
                // at last we are returning our params.
                return params;
            }
        };
        queue.add(request);
    }
}