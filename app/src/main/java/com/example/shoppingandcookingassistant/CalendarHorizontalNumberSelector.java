package com.example.shoppingandcookingassistant;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class CalendarHorizontalNumberSelector extends LinearLayout {

    private TextView numberTV;
    private final int min;
    private final int max;
    private TextView updateTV;
    String date;

    private PortionHorizontalNumberSelector pSel;

    public CalendarHorizontalNumberSelector(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        inflate(context, R.layout.number_selector, this);

        // Define the min and max numbers that can be chosen
        min = 1;
        max = 365;

        numberTV = findViewById(R.id.numberTV);

        Button less = findViewById(R.id.btn_less);
        less.setOnClickListener(new btnPressHandler(-1));

        Button more = findViewById(R.id.btn_more);
        more.setOnClickListener(new btnPressHandler(1));

    }

    private class btnPressHandler implements OnClickListener {

        int iteration;
        public btnPressHandler(int iteration) {
            this.iteration = iteration;
        }

        @Override
        public void onClick(View view) {
            int newNum = getNumber() + iteration;
            if(newNum > max) {
                newNum = max;
            } else if(newNum < min) {
                newNum = min;
            }
            numberTV.setText(String.valueOf(newNum));
            if(!Objects.isNull(updateTV)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, getNumber());
                date = dateFormat.format(c.getTime());
                updateTV.setText(date);
                pSel.updatePortionTV();
            }
        }
    }

    public int getNumber() {
        return Integer.parseInt(numberTV.getText().toString());
    }

    public void setTVAndDate(TextView TV, String date, PortionHorizontalNumberSelector pSel) {
        updateTV = TV;
        this.date = date;
        this.pSel = pSel;
    }

}
