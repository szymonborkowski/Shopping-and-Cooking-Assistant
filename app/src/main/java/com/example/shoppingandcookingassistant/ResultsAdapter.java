package com.example.shoppingandcookingassistant;

/**
 * This is the ResultsAdapter class, defining nested class ViewHolder.
 * Both classes here are used to map each query result to the RecyclerView.
 * @author Anthony Bird
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {
    // instance variables
    private List<Results.ResultsHandler> rhList;
    Context context;

    public ResultsAdapter(Context context, List<Results.ResultsHandler> rhList) {
        this.context = context;
        this.rhList = rhList;
    }

    @Override
    public ResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View resultsView = inflater.inflate(R.layout.populate_results, parent, false);
        ViewHolder viewHolder = new ViewHolder(resultsView);
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
}