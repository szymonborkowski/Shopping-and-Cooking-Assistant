package com.example.shoppingandcookingassistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeRecipeRVAdapter extends RecyclerView.Adapter<HomeRecipeRVAdapter.HomeRVViewHolder> {

    Context context;
    String[] titles, descriptions;

    public static final String RECIPE_NAME = "com.example.shoppingandcookingassistant.RECIPE_NAME";
    public static final String RECIPE_INSTRUCTIONS = "com.example.shoppingandcookingassistant.RECIPE_INSTRUCTIONS";

    RecyclerView recyclerView;

    public HomeRecipeRVAdapter(Context context, String[] titles, String[] descriptions, RecyclerView recyclerView) {
        this.context = context;
        this.titles = titles;
        this.descriptions = descriptions;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public HomeRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.home_recipe_cardview, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildAdapterPosition(v);  // checks which item you clicked
                Intent intent = new Intent(context, DisplayRecipeInstructions.class);  // create a new intent based on opening a recipe
                intent.putExtra(RECIPE_NAME, titles[itemPosition]);
                intent.putExtra(RECIPE_INSTRUCTIONS, descriptions[itemPosition]);
                context.startActivity(intent);
            }
        });
        return new HomeRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRVViewHolder holder, int position) {
        holder.title.setText(titles[position]);
        holder.description.setText(descriptions[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class HomeRVViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;

        public HomeRVViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.homeRecipeName);
            description = itemView.findViewById(R.id.homeRecipeDescription);
        }
    }
}
