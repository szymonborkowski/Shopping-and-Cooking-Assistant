package com.example.shoppingandcookingassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class PlannedMealsRVAdapter extends RecyclerView.Adapter<PlannedMealsRVAdapter.MealsRVViewHolder> {

    Context context;
    ArrayList<String> names, portions, daysLeft, instructions;
    MealsRVViewHolder mealsRVViewHolder;
    Map<String, ArrayList<Meal>> plannedMealsForDate;
    String date;

    public PlannedMealsRVAdapter(Context context, ArrayList<String> names,
                                 ArrayList<String> portions, ArrayList<String> daysLeft,
                                 Map<String, ArrayList<Meal>> plannedMealsForDate,
                                 String date, ArrayList<String> instructions) {
        this.context = context;
        this.names = names;
        this.portions = portions;
        this.daysLeft = daysLeft;
        this.plannedMealsForDate = plannedMealsForDate;
        this.date = date;
        this.instructions = instructions;
    }

    @NonNull
    @Override
    public MealsRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.planned_meal_row, parent, false);
        return new MealsRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealsRVViewHolder holder, int position) {
        mealsRVViewHolder = holder;

        String nameText = "Name: " + names.get(position);
        holder.name.setText(nameText);

        String portionsText = "Portions: " + portions.get(position);
        holder.portions.setText(portionsText);

        String daysLeftText = "Days left: " + daysLeft.get(position);
        holder.daysLeft.setText(daysLeftText);

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                names.remove(position);
                portions.remove(position);
                daysLeft.remove(position);
                instructions.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());

                // take the date and update the map on the given date based
                // on new ArrayLists
                ArrayList<Meal> newPlannedMeals = new ArrayList<>();
                for(int i = 0; i < names.size(); i++) {
                    newPlannedMeals.add(new Meal(names.get(i), portions.get(i), daysLeft.get(i), instructions.get(i)));
                }
                plannedMealsForDate.put(date, newPlannedMeals);
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public ImageButton getRemoveBtn() {
        return mealsRVViewHolder.returnRemoveBtn();
    }

    public class MealsRVViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView portions;
        TextView daysLeft;
        ImageButton removeBtn;

        public MealsRVViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.mealNameTV);
            portions = itemView.findViewById(R.id.mealPortionsTV);
            daysLeft = itemView.findViewById(R.id.mealDaysLeftTV);
            removeBtn = itemView.findViewById(R.id.removeBtn);
        }

        public ImageButton returnRemoveBtn() {
            return removeBtn;
        }
    }
}
