package com.example.shoppingandcookingassistant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class UserFragment extends Fragment {

    RecyclerView recyclerView;
    UserDetailsRVAdapter rvAdapter;
    String[] titles, descriptions;
    Button updateDetails;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.recyclerView);

        titles = getResources().getStringArray(R.array.user_details_titles);
        // titles = view.getResources().getStringArray(R.array.user_details);
        descriptions = getResources().getStringArray(R.array.user_details_entries);

        rvAdapter = new UserDetailsRVAdapter(getActivity(), titles, descriptions);

        recyclerView.setAdapter(rvAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateDetails = view.findViewById(R.id.updateButton);

        updateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Insert SQL update command here
                Toast.makeText(getActivity(), "Details updated", Toast.LENGTH_SHORT).show();
            }
        });

    }
}