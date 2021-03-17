package com.example.grover;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlantAdapterRV extends RecyclerView.Adapter<PlantAdapterRV.ViewHolder> {

    private ArrayList<Plant> mPlants;
    final private OnListItemClickListener mOnListItemClickListener;

    public PlantAdapterRV(ArrayList<Plant> plants, OnListItemClickListener listener){
        mPlants = plants;
        mOnListItemClickListener = listener;
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

        double width = (mPlants.get(position).getDaysBetweenWater() - mPlants.get(position).getDaysSinceLastWater())/mPlants.get(position).getDaysBetweenWater() * 120;

        viewHolder.hydroMeter.setLayoutParams(new CardView.LayoutParams((int) pixelsToDp(viewHolder, (int) width), ViewGroup.LayoutParams.MATCH_PARENT));

        if(mPlants.get(position).waterToday()<=0){
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) viewHolder.waterTExt.getLayoutParams();
            lp.setMargins(0, (int) pixelsToDp(viewHolder, 2), 0, 0);
            viewHolder.waterTExt.setLayoutParams(lp);
            if(mPlants.get(position).waterToday()==0){
                viewHolder.hydroMeterBg.setCardBackgroundColor(viewHolder.context.getColor(R.color.orange));
                viewHolder.waterTExt.setTextColor(viewHolder.context.getColor(R.color.black));
            }
            else{
                viewHolder.hydroMeterBg.setCardBackgroundColor(viewHolder.context.getColor(R.color.red));
                viewHolder.waterTExt.setTextColor(viewHolder.context.getColor(R.color.white));
            }
            viewHolder.waterTExt.setElevation(pixelsToDp(viewHolder, 2));
        }

        if (mPlants.get(position).isFavorite())
            viewHolder.fav.setImageResource(R.drawable.fav);
    }

    public int getItemCount() {
        return mPlants.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView nameLatin;
        TextView waterTExt;
        ImageView icon;
        ImageView fav;
        CardView hydroMeter;
        CardView hydroMeterBg;
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
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(getAdapterPosition());
        }
    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
    private float pixelsToDp(ViewHolder viewHolder, int pixels){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) pixels, viewHolder.context.getResources().getDisplayMetrics());
    }
}
