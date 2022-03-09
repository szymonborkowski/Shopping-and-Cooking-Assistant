package com.example.shoppingandcookingassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;

public class ChooseSearchFiltersActivity extends AppCompatActivity {

    ArrayList<String> cuisineFilters;
    // add more arraylists for each category...

    public static final String CUISINE_FILTERS = "com.example.shoppingandcookingassistant.CUISINE_FILTERS";

    Button saveFiltersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_search_filters);

        cuisineFilters = new ArrayList<>();

        Intent chosenFilters = new Intent();
        chosenFilters.putExtra(CUISINE_FILTERS, cuisineFilters);
        setResult(Activity.RESULT_OK, chosenFilters);

        saveFiltersBtn = findViewById(R.id.saveFiltersBtn);
        saveFiltersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.italianFood_CB:
                if(checked)
                    cuisineFilters.add("Italian");
                else
                    cuisineFilters.remove("Italian");
                break;
            case R.id.asianFood_CB:
                if(checked)
                    cuisineFilters.add("Asian");
                else
                    cuisineFilters.remove("Asian");
                break;
        }

        Intent chosenFilters = new Intent();
        chosenFilters.putExtra(CUISINE_FILTERS, cuisineFilters);
        setResult(Activity.RESULT_OK, chosenFilters);
    }

}