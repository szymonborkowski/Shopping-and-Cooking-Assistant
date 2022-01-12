package com.example.shoppingandcookingassistant;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVViewHolder> {

    Context c;
    String[] s1, s2;

    public RVAdapter(Context c, String[] s1, String[] s2) {
        this.c = c;
        this.s1 = s1;
        this.s2 = s2;
    }

    @NonNull
    @Override
    public RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.item_row, parent, false);

        return new RVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVViewHolder holder, int position) {
        holder.title.setText(s1[position]);
        holder.description.setText(s2[position]);

        holder.description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            // Every time the user updates the text inside the EditText it calls this method
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s2[position] = s.toString();  // updates the entry in the array with new text
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return s1.length;
    }

    public class RVViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        EditText description;

        public RVViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleText);
            description = itemView.findViewById(R.id.editTextTextPersonName);
        }
    }
}

