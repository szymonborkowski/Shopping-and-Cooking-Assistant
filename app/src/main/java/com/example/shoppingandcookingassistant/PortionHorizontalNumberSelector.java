package com.example.shoppingandcookingassistant;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

public class PortionHorizontalNumberSelector extends LinearLayout {

    private TextView numberTV;
    private final int min;
    private final int max;
    private TextView updateTV;
    private String message;
    private CalendarHorizontalNumberSelector cSel;
    int newNum;

    public PortionHorizontalNumberSelector(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        inflate(context, R.layout.number_selector, this);

        min = 1;
        max = 100;

        newNum = 1;  // prevents multiplying by 0

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
            newNum = getNumber() + iteration;
            if(newNum > max) {
                newNum = max;
            } else if(newNum < min) {
                newNum = min;
            }
            numberTV.setText(String.valueOf(newNum));
            if(!Objects.isNull(updateTV)) {
                updatePortionTV();
            }
        }
    }

    public int getNumber() {
        return Integer.parseInt(numberTV.getText().toString());
    }

    public void setTV(TextView TV, CalendarHorizontalNumberSelector cSel) {
        updateTV = TV;
        this.cSel = cSel;
    }

    public void updatePortionTV() {
        int totalPortions = newNum * cSel.getNumber();
        message = "Total portions: " + totalPortions;
        updateTV.setText(message);
    }

}
