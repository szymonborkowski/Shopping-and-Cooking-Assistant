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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    RecyclerView recyclerView;
    RVAdapter rvAdapter;
    String[] titles, descriptions;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();

        return fragment;
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

        rvAdapter = new RVAdapter(getActivity(), titles, descriptions);

        recyclerView.setAdapter(rvAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    public void buttonPress(View view) {
        // SQL command here
    }
}