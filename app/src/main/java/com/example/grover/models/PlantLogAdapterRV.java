package com.example.grover.models;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grover.R;

import java.util.ArrayList;
import java.util.Collections;

public class PlantLogAdapterRV extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<PlantLogItem> log;

    public PlantLogAdapterRV(ArrayList<PlantLogItem> log) {
        this.log = log;
        Collections.reverse(this.log);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_plant_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.text.setText(log.get(position).toString());
        viewHolder.color.setCardBackgroundColor(Color.parseColor(log.get(position).getColor()));
        if (position == 0){
            viewHolder.d3.setVisibility(View.INVISIBLE);
            viewHolder.d4.setVisibility(View.INVISIBLE);
        }
        if(position == log.size()-1){
            viewHolder.d1.setVisibility(View.INVISIBLE);
            viewHolder.d2.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return log.size();
    }
}
class ViewHolder extends RecyclerView.ViewHolder {

    CardView color;
    TextView text;
    View d1;
    View d2;
    View d3;
    View d4;

    ViewHolder(View itemView) {
        super(itemView);
        color = itemView.findViewById(R.id.color);
        text = itemView.findViewById(R.id.textDate);
        d1 = itemView.findViewById(R.id.divider2);
        d2 = itemView.findViewById(R.id.divider3);
        d3 = itemView.findViewById(R.id.divider4);
        d4 = itemView.findViewById(R.id.divider5);
    }
}
