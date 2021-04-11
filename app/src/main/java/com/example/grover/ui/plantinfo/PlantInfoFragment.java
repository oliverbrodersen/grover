package com.example.grover.ui.plantinfo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grover.Plant;
import com.example.grover.PlantAdapterRV;
import com.example.grover.PlantLogAdapterRV;
import com.example.grover.R;
import com.example.grover.data.HomeRepository;
import com.example.grover.ui.home.HomeViewModel;

public class PlantInfoFragment extends Fragment {
    private PlantInfoViewModel viewModel;
    RecyclerView recyclerView;
    PlantLogAdapterRV plantLogAdapterRV;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(PlantInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_plant_info, container, false);

        int plantId = this.getArguments().getInt("PlantId");

        if (plantId >= 0) {

            recyclerView = root.findViewById(R.id.rv);
            recyclerView.hasFixedSize();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            Plant p = HomeRepository.getInstance().getHome().getValue().getPlantsDisplayed().get(plantId);

            plantLogAdapterRV = new PlantLogAdapterRV(p.getLog());
            recyclerView.setAdapter(plantLogAdapterRV);

            ImageView image = root.findViewById(R.id.imageView8);
            TextView hydration = root.findViewById(R.id.hydration);
            TextView name = root.findViewById(R.id.textView5);
            TextView location = root.findViewById(R.id.location);
            TextView waterAmount = root.findViewById(R.id.textView6);
            TextView waterInDays = root.findViewById(R.id.waterInDays);
            TextView daysTillWater = root.findViewById(R.id.daystillwater);
            TextView hydrationTip = root.findViewById(R.id.ttee9);
            TextView latinName = root.findViewById(R.id.textView10);
            CardView currentCardView = root.findViewById(R.id.cardView3);

            image.setImageResource(p.getmIconId());
            name.setText(p.getName());
            latinName.setText(p.getLatinName());
            location.setText(HomeRepository.getInstance().getHome().getValue().getRoomById(p.getRoomId()));
            waterAmount.setText(p.getWaterLevel() + "x");
            waterInDays.setText((int) p.getDaysBetweenWater() + " days");
            daysTillWater.setText(p.waterWhen());

            if (p.waterToday()>0) {
                hydration.setText((int) ((p.getDaysBetweenWater() - p.getDaysSinceLastWater()) / p.getDaysBetweenWater() * 100) + "%");
                currentCardView.setCardBackgroundColor(Color.parseColor("#e1eae1"));
                hydration.setTextColor(Color.parseColor("#567f54"));
                hydrationTip.setTextColor(Color.parseColor("#567f54"));
                daysTillWater.setTextColor(Color.parseColor("#567f54"));
            }
            else {
                hydration.setText("0%");
                if (p.waterToday() == 0){
                    currentCardView.setCardBackgroundColor(Color.parseColor("#efe6bb"));
                    hydration.setTextColor(Color.parseColor("#7f7b54"));
                    hydrationTip.setTextColor(Color.parseColor("#7f7b54"));
                    daysTillWater.setTextColor(Color.parseColor("#7f7b54"));
                }
                else{
                    currentCardView.setCardBackgroundColor(Color.parseColor("#efbbbb"));
                    hydration.setTextColor(Color.parseColor("#7f5454"));
                    hydrationTip.setTextColor(Color.parseColor("#7f5454"));
                    daysTillWater.setTextColor(Color.parseColor("#7f5454"));
                }
            }


        }
        return root;
    }
}
