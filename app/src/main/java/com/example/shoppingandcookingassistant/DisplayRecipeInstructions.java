package com.example.shoppingandcookingassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayRecipeInstructions extends AppCompatActivity {

    public static final String SPECIFIC_RECIPE_INSTRUCTION =
            "com.example.shoppingandcookingassistant.SPECIFIC_RECIPE_INSTRUCTION";
    public static final String SPECIFIC_RECIPE_NUMBER =
            "com.example.shoppingandcookingassistant.SPECIFIC_RECIPE_NUMBER";
    private ArrayList<String> invNames, invAmounts, recipeIngNames, recipeIngAmounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe_instructions);

        Intent intent = getIntent();
        // values from the search fragment class:
        String recipeName = intent.getStringExtra(SearchFragment.RECIPE_NAME);
        String recipeIngredients = intent.getStringExtra(HomeRecipeRVAdapter.RECIPE_INGREDIENTS);
        String recipeInstructions = intent.getStringExtra(SearchFragment.RECIPE_INSTRUCTIONS);

        // set the textView to the chosen recipe name:
        TextView recipeNameTV = findViewById(R.id.recipeNameTV);
        recipeNameTV.setText(recipeName);

        // set the textView to the chosen recipe ingredients:
        TextView recipeIngredientsTV = findViewById(R.id.ingredientsListTV);
        recipeIngredientsTV.setText(recipeIngredients);

        // set the textView to the chosen recipe instructions:
        TextView recipeInstructionsTV = findViewById(R.id.instructionsListTV);
        recipeInstructionsTV.setText(recipeInstructions);

        Button cookBtn = findViewById(R.id.cook_button);
        cookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = getSharedPreferences("com.example.shoppingandcookingassistant.INGREDIENTS", Context.MODE_PRIVATE);
                int size = preferences.getInt(LogInScreenActivity.LOGGED_IN_USER_ID + "_barcodes_size", 0);
                invNames = new ArrayList<>();
                invAmounts = new ArrayList<>();
                for(int i = 0; i < size; i++) {
                    invNames.add(preferences.getString(LogInScreenActivity.LOGGED_IN_USER_ID + "_barcodes_" + i + "_name", null).trim());
                    invAmounts.add(preferences.getString(LogInScreenActivity.LOGGED_IN_USER_ID + "_barcodes_" + i + "_amount", null).trim());
                }

                // get recipe ingredient names and amounts in separate arrays
                recipeIngNames = new ArrayList<>();
                recipeIngAmounts = new ArrayList<>();

                // array of each ingredient:
                String[] ingredientLines = recipeIngredients.split("\n", 0);

                for(String line : ingredientLines) {

                    System.out.println(line);

                    int startName = line.indexOf("•");
                    int endNameStartAmount = line.indexOf("-");

                    String buffName = line.substring(startName + 1, endNameStartAmount - 1).trim();
                    String buffAmount = line.substring(endNameStartAmount + 1).trim();

                    System.out.println("B NAME: " + buffName);
                    System.out.println("B Amount: " + buffAmount);
                    recipeIngNames.add(buffName);
                    recipeIngAmounts.add(buffAmount);
                }

                // alter the amounts that are in the inventory
                for(int i = 0; i < recipeIngNames.size(); i++) {
                    if(invNames.contains(recipeIngNames.get(i).trim())) {
                        String ingredientName = recipeIngNames.get(i).trim();
                        Ingredient buffInvIng = new Ingredient(ingredientName, invAmounts.get(invNames.indexOf(ingredientName)));
                        Ingredient buffRecIng = new Ingredient(ingredientName, recipeIngAmounts.get(i));

                        buffInvIng.decreaseAmount(buffRecIng.getAmountInt());

                        invAmounts.set(invNames.indexOf(ingredientName), buffInvIng.getAmountString());
                    }
                }

                // remove null or negative ingredients
                int i = 0;
                while(i < invNames.size()) {
                    Ingredient buffIng = new Ingredient("buff", invAmounts.get(i));
                    if(buffIng.getAmountInt() <= 0) {
                        invNames.remove(i);
                        invAmounts.remove(i);
                        continue;
                    }
                    i++;
                }

                // save the new inventory
                System.out.println("INV NAMES SIZE: " + invNames.size());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(LogInScreenActivity.LOGGED_IN_USER_ID + "_barcodes_size", invNames.size());
                for(int j = 0; j < invNames.size(); j++) {
                    editor.putString(LogInScreenActivity.LOGGED_IN_USER_ID + "_barcodes_" + j + "_name", invNames.get(j));
                    editor.putString(LogInScreenActivity.LOGGED_IN_USER_ID + "_barcodes_" + j + "_amount", invAmounts.get(j));
                }
                editor.apply();

                // end the activity - the user has cooked the recipe
                finish();
            }

        });

    }


}