package com.example.shoppingandcookingassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BasketListRVAdapter extends RecyclerView.Adapter<BasketListRVAdapter.BasketRVViewHolder> {

    Context context;
    ArrayList<String> ingredients;

    public BasketListRVAdapter(Context context, ArrayList<String> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public BasketRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.basket_ingredient_row, parent, false);
        return new BasketRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketRVViewHolder holder, int position) {
        holder.ingredients.setText(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class BasketRVViewHolder extends RecyclerView.ViewHolder {

        TextView ingredients;

        public BasketRVViewHolder(@NonNull View itemView) {
            super(itemView);

            ingredients = itemView.findViewById(R.id.ingredientTV);
        }
    }

    public void addItemToBasket(String item) {
        ingredients.add(item);
    }

}