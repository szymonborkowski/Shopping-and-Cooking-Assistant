package com.example.shoppingandcookingassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        // TODO: Add a "COOK" button
        // cycles through each instruction, interfaces with raspPi
        Button cookBtn = findViewById(R.id.cook_button);
        cookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // cycle through steps
                String[] instructionsArray = parseInstructionsIntoArray(recipeInstructions);

                // cycle through each instruction:
                for(int i = instructionsArray.length; i > 0; i--) {
                    Intent intent = new Intent(getApplicationContext(), DisplaySpecificInstructionActivity.class);
                    intent.putExtra(SPECIFIC_RECIPE_NUMBER, String.valueOf(i));
                    intent.putExtra(SPECIFIC_RECIPE_INSTRUCTION, instructionsArray[i-1]);
                    startActivity(intent);
                }
            }
        });

    }

    public String[] parseInstructionsIntoArray(String instructionString) {
        ArrayList<String> instructions = new ArrayList<>();

        int i = 1;
        while(instructionString.contains(i+1 + ".")) {
            // add
            int start = instructionString.indexOf(i + ".");
            int end = instructionString.indexOf(i+1 + ".")-1;
            instructions.add(instructionString.substring(start, end));
            i++;
        }
        instructions.add(instructionString.substring(instructionString.indexOf(i + "."),
                                                     instructionString.length()-1));

        return instructions.toArray(new String[0]);
    }
}