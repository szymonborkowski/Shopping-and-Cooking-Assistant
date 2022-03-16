package com.example.shoppingandcookingassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CalendarFragment extends Fragment {

    CalendarView calendar;
    TextView selectedDateTV;
    FloatingActionButton addRecipeBtn;
    String date;
    String message;
    int numPickerVal;

    public static final String CHOSEN_DATE = "com.example.calendarexperiment.DATE";

    RecyclerView plannedMealsListRV;
    PlannedMealsRVAdapter rvAdapter;

    ArrayList<String> names;
    ArrayList<String> portions;
    ArrayList<String> daysLeft;
    ArrayList<Ingredient> ingredientsList;
    ArrayList<ArrayList<Ingredient>> ingredientsForEachMeal;

    Map<String, ArrayList<Meal>> plannedMealsForDate;

    FloatingActionButton addShoppingListBtn;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    // TODO: https://developer.android.com/guide/fragments/saving-state

    /**
     * This method is used to restore any saved variables if there is any.
     * @param savedInstanceState the Bundle which stores the variables.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadMap();
    }

    /**
     * This method is used to retain easily-serialized data.
     * @param outState the Bundle which stores the variables.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveMap();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

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
                            if(Objects.isNull(result.getData())) return;
                            Intent intent = result.getData();
                            // retrieve name, portions, days left
                            String nameBuff = intent.getStringExtra(AddRecipeEvent.CHOSEN_RECIPE);
                            String portionsBuff = intent.getStringExtra(AddRecipeEvent.PORTIONS);
                            String daysLeftBuff = intent.getStringExtra(AddRecipeEvent.DAYS_LEFT);
                            ArrayList<String> ingredientNamesBuff = intent.getStringArrayListExtra(AddRecipeEvent.CHOSEN_RECIPE_INGREDIENT_NAMES);
                            ArrayList<String> ingredientAmountsBuff = intent.getStringArrayListExtra(AddRecipeEvent.CHOSEN_RECIPE_INGREDIENT_AMOUNTS);

                            ingredientsList = new ArrayList<>();
                            for(int i = 0; i < ingredientNamesBuff.size(); i++) {
                                ingredientsList.add(new Ingredient(ingredientNamesBuff.get(i), ingredientAmountsBuff.get(i)));
                            }

                            Meal newMeal = new Meal(nameBuff, portionsBuff, daysLeftBuff, ingredientsList);

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
        ingredientsList = new ArrayList<>();
        ingredientsForEachMeal = new ArrayList<>();

        plannedMealsListRV = view.findViewById(R.id.plannedMealsRV);

        rvAdapter = new PlannedMealsRVAdapter(getActivity(), names, portions, daysLeft, plannedMealsForDate, date, ingredientsForEachMeal);

        plannedMealsListRV.setAdapter(rvAdapter);
        plannedMealsListRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        // *** ADD SHOPPING LIST BUTTON ***
        addShoppingListBtn = view.findViewById(R.id.addShoppingListBtn);
        addShoppingListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open an activity
                showPopupWindow(view);
            }
        });

        clearRV();
        updateRV();

    }

    public void updateRV() {
        int i = 0;
        if(plannedMealsForDate.containsKey(date)) {
            clearRV();
            for (Meal meal : plannedMealsForDate.get(date)) {
                names.add(meal.getName());
                portions.add(meal.getPortions());
                daysLeft.add(meal.getDaysLeft());
                ingredientsForEachMeal.add(meal.getIngredients());
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
            ingredientsForEachMeal.remove(i);
            rvAdapter.notifyItemRemoved(i);
        }
    }

    public void showPopupWindow(final View view) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.add_shopping_list_cardview, null);

        //Number picker
        NumberPicker picker = popupView.findViewById(R.id.numberPicker);
        picker.setMaxValue(7);
        picker.setMinValue(1);
        String[] pickerValues = {"1", "2", "3", "4", "5", "6", "7"};
        picker.setDisplayedValues(pickerValues);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                numPickerVal = picker.getValue();
            }
        });

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        TextView daysQuestionTV = popupView.findViewById(R.id.daysQuestionTV);

        // Close window
        FloatingActionButton closeBtn = popupView.findViewById(R.id.closePopUpBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        // Add shopping list:
        Button saveShoppingListBtn = popupView.findViewById(R.id.saveShoppingListBtn);
        saveShoppingListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // take value from number picker
                // (numPickerVal)

                // look at saved recipes for each day
                // 'date' - today's date

                // variable = Expression1 ? Expression2: Expression3
                int chosenVal = numPickerVal > 0 ? 1 : numPickerVal;
                String recipeNames = "";
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                ArrayList<Ingredient> ingredientsForAllMeals = new ArrayList<>();
                ArrayList<String> names = new ArrayList<>();

                for (int i = 0; i <= chosenVal; i++) {
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DATE, i);
                    date = dateFormat.format(c.getTime());
                    if (plannedMealsForDate.containsKey(date)) {
                        // parse ingredients
                        for(Meal meal : Objects.requireNonNull(plannedMealsForDate.get(date))) {
                            for(Ingredient ingredient : meal.getIngredients()) {
                                if(names.contains(ingredient.getName())) {
                                    ingredientsForAllMeals.get(names.indexOf(ingredient.getName())).increaseAmount(ingredient.getAmountInt());
                                } else {
                                    names.add(ingredient.getName());
                                    ingredientsForAllMeals.add(ingredient);
                                }
                            }
                        }
                    }
                }

                ShoppingList shoppingList = new ShoppingList(getContext(), ingredientsForAllMeals);

                Toast.makeText(getContext(), "Shopping List added", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void saveMap() {
        SharedPreferences preferences = getContext().getSharedPreferences("com.example.shoppingandcookingassistant.MEALS_MAP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();

        // main loop to iterate over each saved date
        int i = 0;
        editor.putInt("date_amount", plannedMealsForDate.size());  // how many dates
        for(Map.Entry<String, ArrayList<Meal>> entry : plannedMealsForDate.entrySet()) {  // each date in map
            String dateKey = "date_" + i++;
            editor.putString(dateKey, date);
            int j = 0;
            editor.putInt(dateKey + "_meal_amount", entry.getValue().size());  // how many meals
            for(Meal meal : entry.getValue()) {  // each meal in date
                editor.putString(dateKey + "_meal_" + j++, gson.toJson(meal));
            }
        }
        editor.apply();
    }

    public void loadMap() {
        // This loop looks at the shared preferences for all the entries associated with
        // a certain date, then the meals for that date, and adds an entry to the map for each
        // date that it finds saved in the shared preferences.
        SharedPreferences preferences = getContext().getSharedPreferences("com.example.shoppingandcookingassistant.MEALS_MAP", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        plannedMealsForDate = new HashMap<>();
        int amountOfDates = preferences.getInt("date_amount", 0);
        for(int i = 0; i < amountOfDates; i++) {
            String buffDate = preferences.getString("date_" + i, "");
            int amountOfMealsForDate = preferences.getInt("date_" + i + "_meal_amount", 0);
            ArrayList<Meal> buffMeals = new ArrayList<>();
            for(int j = 0; j < amountOfMealsForDate; j++) {
                String jsonMeal = preferences.getString("date_" + i + "_meal_" + j, "");
                buffMeals.add(gson.fromJson(jsonMeal, Meal.class));
            }
            plannedMealsForDate.put(buffDate, buffMeals);
        }

    }
}