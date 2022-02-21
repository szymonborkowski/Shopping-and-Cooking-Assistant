package com.example.shoppingandcookingassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class ChooseSearchFiltersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_search_filters);



    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkBox1:
                if(checked)
                    System.out.println("1 is checked");
                else
                    System.out.println("1 is unchecked");
                break;
            case R.id.checkBox2:
                if(checked)
                    System.out.println("2 is checked");
                else
                    System.out.println("2 is unchecked");
                break;
        }
    }

}