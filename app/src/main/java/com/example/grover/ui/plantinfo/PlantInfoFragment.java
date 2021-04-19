package com.example.grover.ui.plantinfo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grover.models.Plant;
import com.example.grover.models.PlantLogAdapterRV;
import com.example.grover.R;
import com.example.grover.data.HomeRepository;
import com.example.grover.models.trefle.trefleSpeciesComplete.Data;

public class PlantInfoFragment extends Fragment {
    private PlantInfoViewModel viewModel;
    RecyclerView recyclerView;
    PlantLogAdapterRV plantLogAdapterRV;
    Plant p;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(PlantInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_plant_info, container, false);

        int plantId = this.getArguments().getInt("PlantId");
        String plantName = this.getArguments().getString("PlantName");

        if (plantName != null){
            p = viewModel.getPlantFromName(plantName);
            Log.d("tag", p.getName());
        }
        else if (plantId >= 0) {

            recyclerView = root.findViewById(R.id.rv);
            recyclerView.hasFixedSize();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            p = HomeRepository.getInstance().getHome().getValue().getPlantsDisplayed().get(plantId);
        }
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
            TextView trefleHeader = root.findViewById(R.id.textView19);
            TextView notesHeader = root.findViewById(R.id.textViewNotesTitle);
            TextView notesText = root.findViewById(R.id.textView18);

            CardView trefleCard = root.findViewById(R.id.cardView6);
            CardView notesCard = root.findViewById(R.id.cardViewNotes);
            CardView currentCardView = root.findViewById(R.id.cardView3);

            //Trefle
            TextView commonName = root.findViewById(R.id.commonName);
            TextView commonNameLable = root.findViewById(R.id.lableCommonName);
            TextView slug = root.findViewById(R.id.slug);
            TextView slugLable = root.findViewById(R.id.lableSlug);
            TextView scientific = root.findViewById(R.id.scientific);
            TextView scientificLable = root.findViewById(R.id.lableScientific);
            TextView family = root.findViewById(R.id.family);
            TextView familyLable = root.findViewById(R.id.lableFamily);



            if (p.getImgUri() != null)
                image.setImageURI(p.getImgUri());
            else
                image.setImageResource(p.getmIconId());

            name.setText(p.getName());
            latinName.setText(p.getLatinName());
            location.setText(HomeRepository.getInstance().getHome().getValue().getRoomById(p.getRoomId()));
            waterAmount.setText(p.getWaterLevel() + "x");
            waterInDays.setText((int) p.getDaysBetweenWater() + " days");
            daysTillWater.setText(p.waterWhen());

            if (p.getTrefleId() != 0){
                //trefle.setText(p.getTreflePlantInfo().getFamily_common_name());
                trefleHeader.setVisibility(View.VISIBLE);
                trefleCard.setVisibility(View.VISIBLE);
                //Common name
                if (p.getTreflePlantInfo().getCommon_name() != null){
                    commonName.setText(p.getTreflePlantInfo().getCommon_name());
                    commonName.setVisibility(View.VISIBLE);
                    commonNameLable.setVisibility(View.VISIBLE);
                }
                else {
                    commonName.setVisibility(View.GONE);
                    commonNameLable.setVisibility(View.GONE);
                }
                //Slug
                if (p.getTreflePlantInfo().getSlug() != null){
                    slug.setText(p.getTreflePlantInfo().getSlug());
                    slug.setVisibility(View.VISIBLE);
                    slugLable.setVisibility(View.VISIBLE);
                }
                else {
                    slug.setVisibility(View.GONE);
                    slugLable.setVisibility(View.GONE);
                }
                //Scientific
                if (p.getTreflePlantInfo().getScientific_name() != null){
                    scientific.setText(p.getTreflePlantInfo().getScientific_name());
                    scientific.setVisibility(View.VISIBLE);
                    scientificLable.setVisibility(View.VISIBLE);
                }
                else {
                    scientific.setVisibility(View.GONE);
                    scientificLable.setVisibility(View.GONE);
                }
                //Family
                if (p.getTreflePlantInfo().getFamily_common_name() != null){
                    family.setText(p.getTreflePlantInfo().getFamily_common_name());
                    family.setVisibility(View.VISIBLE);
                    familyLable.setVisibility(View.VISIBLE);
                }
                else {
                    family.setVisibility(View.GONE);
                    familyLable.setVisibility(View.GONE);
                }
            }
            else{
                trefleHeader.setVisibility(View.GONE);
                trefleCard.setVisibility(View.GONE);
            }

            if (p.getNote().equals("") || p.getNote() == null){
                notesCard.setVisibility(View.GONE);
                notesHeader.setVisibility(View.GONE);
            }
            else{
                notesText.setText(p.getNote());

                notesCard.setVisibility(View.VISIBLE);
                notesHeader.setVisibility(View.VISIBLE);
            }

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



        return root;
    }
}