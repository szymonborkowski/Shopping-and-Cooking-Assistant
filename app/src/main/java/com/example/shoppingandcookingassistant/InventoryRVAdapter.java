package com.example.shoppingandcookingassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InventoryRVAdapter extends RecyclerView.Adapter<InventoryRVAdapter.RVViewHolder> {

    Context c;
    String[] s1;

    public InventoryRVAdapter(Context c, String[] s1) {
        this.c = c;
        this.s1 = s1;
    }

    @NonNull
    @Override
    public RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.ingredient_row, parent, false);
        return new RVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVViewHolder holder, int position) {
        holder.barcode.setText(s1[position]);
    }

    @Override
    public int getItemCount() {
        return s1.length;
    }

    public class RVViewHolder extends RecyclerView.ViewHolder {

        TextView barcode;

        public RVViewHolder(@NonNull View itemView) {
            super(itemView);

            barcode = itemView.findViewById(R.id.ingredientRowTV);
        }
    }
}
