package com.example.shoppingandcookingassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;

public class ShoppingListsActivity extends AppCompatActivity {

    ArrayList<String> ingredientName;
    ArrayList<String> ingredientAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_lists);
        loadArray();

        String[] listItems = new String[ingredientName.size()];
        for(int i = 0; i < ingredientName.size(); i++) {
            listItems[i] = ingredientName.get(i) + " - " + ingredientAmount.get(i);
        }

        InventoryRVAdapter rvAdapter = new InventoryRVAdapter(getApplicationContext(), listItems);
        RecyclerView recyclerView = findViewById(R.id.shoppingListRV);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void loadArray() {
        SharedPreferences preferences = getSharedPreferences("com.example.shoppingandcookingassistant.SHOPPING_LISTS", Context.MODE_PRIVATE);
        int size = preferences.getInt("ingredients_size", 0);
        ingredientName = new ArrayList<>();
        ingredientAmount = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            ingredientName.add(preferences.getString("ingredientName_" + i, null));
            ingredientAmount.add(preferences.getString("ingredientAmount_" + i, null));
        }
    }

    /*
    public void saveShoppingList() {
        SharedPreferences preferences = context.getSharedPreferences("com.example.shoppingandcookingassistant.SHOPPING_LISTS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("ingredients_size", ingredients.size());
        for(int i = 0; i < ingredients.size(); i++) {
            editor.putString("ingredientName_" + i, ingredients.get(i).getName());
            editor.putString("ingredientAmount_" + i, ingredients.get(i).getAmountString());
        }
        editor.apply();
    }
     */
}