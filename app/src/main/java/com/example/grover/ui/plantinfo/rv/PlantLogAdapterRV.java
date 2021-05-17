package com.example.grover.ui.plantinfo.rv;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grover.R;
import com.example.grover.data.HomeRepository;
import com.example.grover.models.PlantLogItem;

import java.util.ArrayList;

public class PlantLogAdapterRV extends RecyclerView.Adapter<PlantLogAdapterRV.ViewHolder> {
    private ArrayList<PlantLogItem> log;
    private String plantId;
    final private OnListItemLongClickListener mOnListItemLongClickListener;

    public PlantLogAdapterRV(ArrayList<PlantLogItem> log, OnListItemLongClickListener listener, String plantId) {
        this.log = log;
        this.plantId = plantId;
        //Collections.reverse(this.log);
        mOnListItemLongClickListener = listener;
    }

    public void setNewDataSet(ArrayList<PlantLogItem> newLog){
        log = newLog;
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
        log.get(position).getLongpressed().observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    viewHolder.deleteButton.setVisibility(View.VISIBLE);
                else
                    viewHolder.deleteButton.setVisibility(View.GONE);
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                HomeRepository.getInstance().getHome().getValue().getPlantById(plantId).closeAllLogItems();
                //HomeRepository.getInstance().updateDatabase();
                //setNewDataSet(HomeRepository.getInstance().getHome().getValue().getPlantById(plantId).getLog());
            }
        });

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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        CardView color;
        ImageView deleteButton;
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
            deleteButton = itemView.findViewById(R.id.buttonDelete);
            d1 = itemView.findViewById(R.id.divider2);
            d2 = itemView.findViewById(R.id.divider3);
            d3 = itemView.findViewById(R.id.divider4);
            d4 = itemView.findViewById(R.id.divider5);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            mOnListItemLongClickListener.onListItemLongClick(getAdapterPosition());
            return true;
        }
    }

    public interface OnListItemLongClickListener {
        void onListItemLongClick(int clickedItemIndex);
    }
}
