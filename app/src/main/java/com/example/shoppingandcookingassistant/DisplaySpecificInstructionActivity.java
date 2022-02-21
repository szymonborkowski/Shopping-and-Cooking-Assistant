package com.example.shoppingandcookingassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplaySpecificInstructionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_specific_instruction);

        Button nextStepBtn = findViewById(R.id.nextStepButton);
        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // instructionNoTV
        // specificInstructionTV

        Intent intent = getIntent();

        TextView instructionNoTV = findViewById(R.id.instructionNoTV);
        String instructionNo = "Instruction No: " + intent.getStringExtra(DisplayRecipeInstructions.SPECIFIC_RECIPE_NUMBER);
        instructionNoTV.setText(instructionNo);

        TextView specificInstructionTV = findViewById(R.id.specificInstructionTV);
        specificInstructionTV.setText(intent.getStringExtra(DisplayRecipeInstructions.SPECIFIC_RECIPE_INSTRUCTION));

    }
}