package com.example.shoppingandcookingassistant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {

    public TextView monthYearText;
    public RecyclerView calendarRV;
    public LocalDate pressedDate;
    public Button leftBtn;
    public Button rightBtn;

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
        leftBtn = view.findViewById(R.id.leftBtn);
        rightBtn = view.findViewById(R.id.rightBtn);

        // Set the methods that run when buttons are pressed:
        leftBtn.setOnClickListener(this::leftArrow);
        rightBtn.setOnClickListener(this::rightArrow);

        calendarRV = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
        pressedDate = LocalDate.now();

        setMonthView();

    }

    public void setMonthView() {
        // Set the correct title at the top of the fragment:
        monthYearText.setText(pressedDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")));

        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(pressedDate);
        int daysInMonth = yearMonth.lengthOfMonth();

        int dayOfWeek = pressedDate.withDayOfMonth(1).getDayOfWeek().getValue();

        // Create layout of dates:
        for(int i = 1; i <= 42; i++) {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonthArray, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 7);
        calendarRV.setLayoutManager(layoutManager);
        calendarRV.setAdapter(calendarAdapter);
    }

    // When left arrow pressed:
    public void leftArrow(View view) {
        pressedDate = pressedDate.minusMonths(1);
        setMonthView();
    }

    // When right arrow pressed:
    public void rightArrow(View view) {
        pressedDate = pressedDate.plusMonths(1);
        setMonthView();
    }

    // When a date is selected:
    @Override
    public void onItemClick(int position, String dayText) {
        if(!dayText.equals("")) {
            String message = "Selected Date " + dayText + " " + pressedDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }
}