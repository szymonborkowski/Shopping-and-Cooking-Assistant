package com.example.shoppingandcookingassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddRecipeEvent extends AppCompatActivity {

    Button saveRecipe;

    EditText recipeName;
    TextView lastDate;
    TextView totalPortions;

    CalendarHorizontalNumberSelector daysSelector;
    PortionHorizontalNumberSelector portionsSelector;

    public static final String CHOSEN_RECIPE = "com.example.calendarexperiment.CHOSEN_RECIPE";
    public static final String PORTIONS = "com.example.calendarexperiment.PORTIONS";
    public static final String DAYS_LEFT = "com.example.calendarexperiment.DAYS_LEFT";

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
                setResult(Activity.RESULT_OK, addRecipe);
                finish();
            }
        });

        recipeName = findViewById(R.id.recipeSearchView);

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
    }
}