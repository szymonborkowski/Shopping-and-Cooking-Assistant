package com.example.shoppingandcookingassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class UserDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    UserDetailsRVAdapter rvAdapter;
    String[] titles, descriptions;
    Button updateDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        recyclerView = findViewById(R.id.recyclerView);

        titles = getResources().getStringArray(R.array.user_details_titles);
        // titles = view.getResources().getStringArray(R.array.user_details);
        descriptions = getResources().getStringArray(R.array.user_details_entries);

        rvAdapter = new UserDetailsRVAdapter(getApplicationContext(), titles, descriptions);

        recyclerView.setAdapter(rvAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        updateDetails = findViewById(R.id.updateButton);

        updateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Insert SQL update command here
                Toast.makeText(getApplicationContext(), "Details updated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}