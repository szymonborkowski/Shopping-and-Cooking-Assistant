package com.example.shoppingandcookingassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddRecipeEvent extends AppCompatActivity {

    Button saveRecipe;

    TextView recipeName;
    TextView lastDate;
    TextView totalPortions;
    Button changeRecipeBtn;
    String chosenRecipeInstructions;

    CalendarHorizontalNumberSelector daysSelector;
    PortionHorizontalNumberSelector portionsSelector;

    public static final String CHOSEN_RECIPE = "com.example.calendarexperiment.CHOSEN_RECIPE";
    public static final String PORTIONS = "com.example.calendarexperiment.PORTIONS";
    public static final String DAYS_LEFT = "com.example.calendarexperiment.DAYS_LEFT";
    public static final String CHOSEN_RECIPE_INSTRUCTIONS = "com.example.calendarexperiment.CHOSEN_RECIPE_INSTRUCTIONS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_event);

        Intent intent = getIntent();
        String date = intent.getStringExtra(CalendarFragment.CHOSEN_DATE);

        saveRecipe = findViewById(R.id.saveRecipeEventBtn);

        saveRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send over a prompt to add a recipe to a list
                Intent addRecipe = new Intent();
                addRecipe.putExtra(CHOSEN_RECIPE, recipeName.getText().toString());
                addRecipe.putExtra(PORTIONS, totalPortions.getText());
                addRecipe.putExtra(DAYS_LEFT, String.valueOf(daysSelector.getNumber()));
                addRecipe.putExtra(CHOSEN_RECIPE_INSTRUCTIONS, chosenRecipeInstructions);
                setResult(Activity.RESULT_OK, addRecipe);
                finish();
            }
        });

        recipeName = findViewById(R.id.chosenRecipeTV);

        lastDate = findViewById(R.id.lastDateTV);
        totalPortions = findViewById(R.id.portionsTV);

        daysSelector = findViewById(R.id.daysSelector);

        portionsSelector = findViewById(R.id.peopleSelector);
        portionsSelector.setTV(totalPortions, daysSelector);

        daysSelector.setTVAndDate(lastDate, date, portionsSelector);

        // move into calhozselector?
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        date = dateFormat.format(c.getTime());

        lastDate.setText(date);

        String message = "Total portions: 1";
        totalPortions.setText(message);

        changeRecipeBtn = findViewById(R.id.changeRecipeBtn);
        changeRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchRecipeActivity.class);
                activityResultLauncher.launch(intent);

                // on result get chosen recipe

                // TODO: ON RESULT METHOD SET TV TO XYZ
            }
            ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            Intent intent = result.getData();
                            String selectedRecipe = intent.getStringExtra(SearchRecipeActivity.SELECTED_RECIPE_BY_USER);
                            chosenRecipeInstructions = intent.getStringExtra(SearchRecipeActivity.SELECTED_RECIPE_INGREDIENTS);
                            recipeName.setText(selectedRecipe);
                        }
                    }
            );
        });
    }
}