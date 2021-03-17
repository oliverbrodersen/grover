package com.example.grover;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlantAdapterRV extends RecyclerView.Adapter<PlantAdapterRV.ViewHolder> {

    private ArrayList<Plant> mPlants;
    final private OnListItemClickListener mOnListItemClickListener;
    final private OnListItemLongClickListener mOnListItemLongClickListener;

    public PlantAdapterRV(ArrayList<Plant> plants, OnListItemClickListener listener, OnListItemLongClickListener listener2){
        mPlants = plants;
        mOnListItemClickListener = listener;
        mOnListItemLongClickListener = listener2;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.listi_tem_plant_card, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.name.setText(mPlants.get(position).getName());
        viewHolder.nameLatin.setText(mPlants.get(position).getLatinName());
        viewHolder.icon.setImageResource(mPlants.get(position).getmIconId());
        viewHolder.waterTExt.setText(mPlants.get(position).waterWhen());

        //If plant needs watering
        if(mPlants.get(position).waterToday()<=0){
            if(mPlants.get(position).waterToday()==0)
                viewHolder.hydroMeterBg.setImageResource(R.drawable.orange_gradient);
            else
                viewHolder.hydroMeterBg.setImageResource(R.drawable.red_gradient);

            //Move text up
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) viewHolder.waterTExt.getLayoutParams();
            lp.setMargins(0, 0, 0, 0);
            viewHolder.waterTExt.setLayoutParams(lp);

            //Change text color and size
            viewHolder.waterTExt.setTextColor(viewHolder.context.getColor(R.color.white));
            viewHolder.waterTExt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

            //Remove hydrometer
            viewHolder.hydroMeter.setLayoutParams(new FrameLayout.LayoutParams(0, (int) pixelsToDp(viewHolder, 15)));

            viewHolder.waterTExt.setElevation(pixelsToDp(viewHolder, 2));
        }
        else{
            double width = (mPlants.get(position).getDaysBetweenWater() - mPlants.get(position).getDaysSinceLastWater())/mPlants.get(position).getDaysBetweenWater() * 120;
            viewHolder.hydroMeter.setLayoutParams(new FrameLayout.LayoutParams((int) pixelsToDp(viewHolder, (int) width), (int) pixelsToDp(viewHolder, 15)));
        }

        if (mPlants.get(position).isFavorite())
            viewHolder.fav.setImageResource(R.drawable.fav);
    }

    public int getItemCount() {
        return mPlants.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView name;
        TextView nameLatin;
        TextView waterTExt;
        ImageView icon;
        ImageView fav;
        CardView hydroMeter;
        ImageView hydroMeterBg;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.plantName);
            nameLatin = itemView.findViewById(R.id.textView4);
            waterTExt = itemView.findViewById(R.id.waterText);
            icon = itemView.findViewById(R.id.imageView2);
            fav = itemView.findViewById(R.id.fav);
            hydroMeter = itemView.findViewById(R.id.hydroMeter);
            hydroMeterBg = itemView.findViewById(R.id.hydroMeterBg);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(getAdapterPosition());
        }
        @Override
        public boolean onLongClick(View v) {
            mOnListItemLongClickListener.onListItemLongClick(getAdapterPosition());
            return true;
        }
    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
    public interface OnListItemLongClickListener {
        void onListItemLongClick(int clickedItemIndex);
    }
    private float pixelsToDp(ViewHolder viewHolder, int pixels){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) pixels, viewHolder.context.getResources().getDisplayMetrics());
    }
}
