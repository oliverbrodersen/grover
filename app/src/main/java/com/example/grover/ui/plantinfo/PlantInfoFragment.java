package com.example.grover.ui.plantinfo;

import android.graphics.Color;
import android.net.Uri;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.grover.models.Home;
import com.example.grover.models.Plant;
import com.example.grover.models.PlantLogAdapterRV;
import com.example.grover.R;
import com.example.grover.data.HomeRepository;
import com.example.grover.ui.addplant.AddPlantFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PlantInfoFragment extends Fragment implements PlantLogAdapterRV.OnListItemClickListener {
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

        String plantId = this.getArguments().getString("PlantId");
        p = HomeRepository.getInstance().getHome().getValue().getPlantById(plantId);

        recyclerView = root.findViewById(R.id.rv);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            plantLogAdapterRV = new PlantLogAdapterRV(p.getLog(), this);
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
            CardView waterButton = root.findViewById(R.id.waterButton);
            CardView editButton = root.findViewById(R.id.editButton);

            //Trefle
            TextView commonName = root.findViewById(R.id.commonName);
            TextView commonNameLable = root.findViewById(R.id.lableCommonName);
            TextView slug = root.findViewById(R.id.slug);
            TextView slugLable = root.findViewById(R.id.lableSlug);
            TextView scientific = root.findViewById(R.id.scientific);
            TextView scientificLable = root.findViewById(R.id.lableScientific);
            TextView family = root.findViewById(R.id.family);
            TextView familyLable = root.findViewById(R.id.lableFamily);

            //Download and view image
            StorageReference ref = FirebaseStorage.getInstance().getReference();
            ref.child("images/" + p.getImageId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    //viewHolder.icon.setImageURI(uri);
                    Glide.with(getContext())
                            .load(uri)
                            .into(image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Log.e("tag", exception.toString());
                }
            });

            name.setText(p.getName());
            latinName.setText(p.getLatinName());
            location.setText(HomeRepository.getInstance().getHome().getValue().getRoomById(p.getRoomId()));
            waterAmount.setText(p.getWaterLevel() + "x");
            waterInDays.setText((int) p.getDaysBetweenWater() + " days");
            daysTillWater.setText(p.waterWhen());
            waterButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Home h = viewModel.getHome().getValue();
                    ArrayList<Plant> plantArrayList = h.getPlants();
                    for (Plant plant:plantArrayList) {
                        if (plant.getName().equals(p.getName()))
                        {
                            plant.water();
                            break;
                        }
                    }
                    h.setPlants(plantArrayList);
                    viewModel.updateHome(h);


                    FragmentTransaction fragmentTransaction = getActivity()
                            .getSupportFragmentManager().beginTransaction();
                    PlantInfoFragment fragment = new PlantInfoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("PlantName", p.getName());
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                    fragmentTransaction.commit();
                }
            });

            if (p.getTrefleId() != 0 && p.getTreflePlantInfo() != null){
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

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                AddPlantFragment fragment = new AddPlantFragment();
                Bundle bundle = new Bundle();
                bundle.putString("PlantId", p.getPlantId());
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack( "tag" );
                fragmentTransaction.commit();
            }
        });

        return root;
    }

    @Override
    public void OnClickListener(int clickedItemIndex) {
        Log.d("log", "Long pressed: " + p.getLog().get(clickedItemIndex));
    }
}
