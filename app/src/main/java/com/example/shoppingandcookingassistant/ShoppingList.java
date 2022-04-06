package com.example.shoppingandcookingassistant;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ShoppingList {

    ArrayList<Ingredient> ingredients;
    Context context;

    public ShoppingList(Context context, ArrayList<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
        saveShoppingList();
    }

    public void saveShoppingList() {
        SharedPreferences preferences = context.getSharedPreferences("com.example.shoppingandcookingassistant.SHOPPING_LISTS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LogInScreenActivity.LOGGED_IN_USER_ID + "_ingredients_size", ingredients.size());
        for(int i = 0; i < ingredients.size(); i++) {
            editor.putString(LogInScreenActivity.LOGGED_IN_USER_ID + "_ingredientName_" + i, ingredients.get(i).getName());
            editor.putString(LogInScreenActivity.LOGGED_IN_USER_ID + "_ingredientAmount_" + i, ingredients.get(i).getAmountString());
        }
        editor.apply();
    }

}
