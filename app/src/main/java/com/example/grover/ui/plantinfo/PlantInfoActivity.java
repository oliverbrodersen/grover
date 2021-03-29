package com.example.grover.ui.plantinfo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grover.Plant;
import com.example.grover.PlantAdapterRV;
import com.example.grover.PlantLogAdapterRV;
import com.example.grover.R;
import com.example.grover.data.HomeRepository;
import com.example.grover.ui.slideshow.SlideshowViewModel;
import com.google.android.material.navigation.NavigationView;

public class PlantInfoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PlantLogAdapterRV plantLogAdapterRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LightTheme);
        setContentView(R.layout.fragment_plant_info);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey("PlantId")) {

            recyclerView = findViewById(R.id.rv);
            recyclerView.hasFixedSize();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            int plantId = bundle.getInt("PlantId");
            Plant p = HomeRepository.getInstance().getHome().getValue().getPlantsDisplayed().get(plantId);

            plantLogAdapterRV = new PlantLogAdapterRV(p.getLog());
            recyclerView.setAdapter(plantLogAdapterRV);

            ImageView image = findViewById(R.id.imageView8);
            TextView hydration = findViewById(R.id.hydration);
            TextView name = findViewById(R.id.textView5);
            TextView location = findViewById(R.id.location);
            TextView waterAmount = findViewById(R.id.textView6);
            TextView waterInDays = findViewById(R.id.waterInDays);
            TextView daysTillWater = findViewById(R.id.daystillwater);
            TextView hydrationTip = findViewById(R.id.ttee9);
            TextView latinName = findViewById(R.id.textView10);
            CardView currentCardView = findViewById(R.id.cardView3);

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
    }
}