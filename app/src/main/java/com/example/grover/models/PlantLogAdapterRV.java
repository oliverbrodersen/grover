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

public class PlantLogAdapterRV extends RecyclerView.Adapter<PlantLogAdapterRV.ViewHolder> {
    private ArrayList<PlantLogItem> log;
    final private PlantLogAdapterRV.OnListItemClickListener mOnListItemClickListener;

    public PlantLogAdapterRV(ArrayList<PlantLogItem> log, PlantLogAdapterRV.OnListItemClickListener mOnListItemClickListener) {
        this.log = log;
        this.mOnListItemClickListener = mOnListItemClickListener;
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
        viewHolder.d1.setVisibility(View.VISIBLE);
        viewHolder.d2.setVisibility(View.VISIBLE);
        viewHolder.d3.setVisibility(View.VISIBLE);
        viewHolder.d4.setVisibility(View.VISIBLE);
        if (position == 0){
            viewHolder.d3.setVisibility(View.INVISIBLE);
            viewHolder.d4.setVisibility(View.INVISIBLE);
        }
        if(position == log.size()-1){
            viewHolder.d1.setVisibility(View.INVISIBLE);
            viewHolder.d2.setVisibility(View.INVISIBLE);
        }
        if (log.get(position).getNote() != null){
            viewHolder.note.setVisibility(View.VISIBLE);
            viewHolder.note.setText(log.get(position).getNote());
        }
        else {
            viewHolder.note.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return log.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView color;
        TextView text;
        TextView note;
        View d1;
        View d2;
        View d3;
        View d4;

        ViewHolder(View itemView) {
            super(itemView);
            color = itemView.findViewById(R.id.color);
            text = itemView.findViewById(R.id.textDate);
            note = itemView.findViewById(R.id.note);
            d1 = itemView.findViewById(R.id.divider2);
            d2 = itemView.findViewById(R.id.divider3);
            d3 = itemView.findViewById(R.id.divider4);
            d4 = itemView.findViewById(R.id.divider5);
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.OnClickListener(getAdapterPosition());
        }
    }
    public interface OnListItemClickListener {
        void OnClickListener(int clickedItemIndex);
    }
}
