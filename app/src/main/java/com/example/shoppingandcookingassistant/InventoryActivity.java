package com.example.shoppingandcookingassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class InventoryActivity extends AppCompatActivity {

    InventoryRVAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        String[] barcodesFromMemory = loadArray("barcodes");
        rvAdapter = new InventoryRVAdapter(getApplicationContext(), barcodesFromMemory);

        RecyclerView inventoryRV = findViewById(R.id.inventoryRV);
        inventoryRV.setAdapter(rvAdapter);
        inventoryRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    public String[] loadArray(String arrayName) {
        SharedPreferences preferences = getSharedPreferences("com.example.shoppingandcookingassistant.INGREDIENTS", Context.MODE_PRIVATE);
        int size = preferences.getInt(LogInScreenActivity.LOGGED_IN_USER_ID + "_" + arrayName + "_size", 0);
        String[] array = new String[size];
        for(int i = 0; i < size; i++) {
            array[i] = " • " + preferences.getString(LogInScreenActivity.LOGGED_IN_USER_ID + "_" + arrayName + "_" + i + "_name", null);
            array[i] += " - " + preferences.getString(LogInScreenActivity.LOGGED_IN_USER_ID + "_" + arrayName + "_" + i + "_amount", null);
                                                    //   LogInScreenActivity.LOGGED_IN_USER_ID + "_barcodes_" + i + "_amount"
        }
        return array;
    }
}

