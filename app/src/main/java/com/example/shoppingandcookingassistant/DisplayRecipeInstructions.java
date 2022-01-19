package com.example.shoppingandcookingassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayRecipeInstructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe_instructions);

        Intent intent = getIntent();
        // values from the search fragment class:
        String recipeName = intent.getStringExtra(SearchFragment.RECIPE_NAME);
        String recipeInstructions = intent.getStringExtra(SearchFragment.RECIPE_INSTRUCTIONS);

        // set the textView to the chosen recipe name:
        TextView recipeNameTV = findViewById(R.id.recipeNameTV);
        recipeNameTV.setText(recipeName);

        // set the textView to the chosen recipe instructions:
        TextView recipeInstructionsTV = findViewById(R.id.recipeInstructionsTV);
        recipeInstructionsTV.setText(recipeInstructions);

    }
}