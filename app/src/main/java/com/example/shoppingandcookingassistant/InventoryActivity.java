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

    // https://stackoverflow.com/questions/12350800/android-how-to-store-array-of-strings-in-sharedpreferences-for-android

    public String[] loadArray(String arrayName) {
        SharedPreferences preferences = getSharedPreferences("com.example.shoppingandcookingassistant.INGREDIENTS", Context.MODE_PRIVATE);
        int size = preferences.getInt(arrayName + "_size", 0);
        String[] array = new String[size];
        for(int i = 0; i < size; i++)
            array[i] = preferences.getString(arrayName + "_" + i, null);
        return array;
    }
}