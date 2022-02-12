package com.example.shoppingandcookingassistant;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CalendarFragment extends Fragment {

    CalendarView calendar;
    TextView selectedDateTV;
    FloatingActionButton addRecipeBtn;
    String date;
    String message;

    public static final String CHOSEN_DATE = "com.example.calendarexperiment.DATE";

    RecyclerView plannedMealsListRV;
    PlannedMealsRVAdapter rvAdapter;

    ArrayList<String> names;
    ArrayList<String> portions;
    ArrayList<String> daysLeft;

    Map<String, ArrayList<Meal>> plannedMealsForDate;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        plannedMealsForDate = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        date = sdf.format(new Date(System.currentTimeMillis()));  // setting date value to today
        message = "Selected date: " + date;

        selectedDateTV = view.findViewById(R.id.selectedDateTV);
        selectedDateTV.setText(message);

        calendar = view.findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                month += 1;
                String dayString = String.valueOf(day);
                String monthString = String.valueOf(month);

                // adding '0's to month to make the dates consistent
                if(day <= 9) {
                    dayString = "0" + day;
                }
                if(month <= 9) {
                    monthString = "0" + month;
                }

                date = dayString + "/" + monthString + "/" + year;
                message = "Selected date: " + date;
                selectedDateTV.setText(message);

                // Reload the array that will hold meal objects
                // this will change the RecyclerView that displays planned meals for date
                clearRV();
                updateRV();
            }
        });

        addRecipeBtn = view.findViewById(R.id.addRecipeBtn);
        addRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddRecipeEvent.class);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                // String chosenDate = sdf.format(new Date(calendar.getDate()));

                try {
                    date = sdf.format(new Date(String.valueOf(sdf.parse(date))));
                } catch (ParseException e) {e.printStackTrace();}

                intent.putExtra(CHOSEN_DATE, date);

                activityResultLauncher.launch(intent);

            }

            // decides what happens when the activity is closed (meal is added)
            ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            Intent intent = result.getData();
                            // retrieve name, portions, days left
                            String nameBuff = intent.getStringExtra(AddRecipeEvent.CHOSEN_RECIPE);
                            String portionsBuff = intent.getStringExtra(AddRecipeEvent.PORTIONS);
                            String daysLeftBuff = intent.getStringExtra(AddRecipeEvent.DAYS_LEFT);

                            Meal newMeal = new Meal(nameBuff, portionsBuff, daysLeftBuff);

                            ArrayList<Meal> newPlannedMeals;

                            // get old ArrayList if there is one
                            if(plannedMealsForDate.containsKey(date)) {
                                newPlannedMeals = plannedMealsForDate.get(date);
                            } else {  // otherwise add new list of meals for date
                                newPlannedMeals = new ArrayList<>();
                            }
                            newPlannedMeals.add(newMeal);
                            plannedMealsForDate.put(date, newPlannedMeals);  // update entry at date
                            updateRV();
                        }
                    }
            );
        });

        names = new ArrayList<>();
        portions = new ArrayList<>();
        daysLeft = new ArrayList<>();
        plannedMealsListRV = view.findViewById(R.id.plannedMealsRV);

        rvAdapter = new PlannedMealsRVAdapter(getActivity(), names, portions, daysLeft, plannedMealsForDate, date);

        plannedMealsListRV.setAdapter(rvAdapter);
        plannedMealsListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void updateRV() {
        int i = 0;
        if(plannedMealsForDate.containsKey(date)) {
            clearRV();
            for (Meal meal : plannedMealsForDate.get(date)) {
                names.add(meal.getName());
                portions.add(meal.getPortions());
                daysLeft.add(meal.getDaysLeft());
                rvAdapter.notifyItemInserted(i++);
            }
        }
    }

    public void clearRV() {
        int size = names.size() - 1;
        for(int i = size; i >= 0; i--) {
            names.remove(i);
            portions.remove(i);
            daysLeft.remove(i);
            rvAdapter.notifyItemRemoved(i);
        }
    }
}