package com.example.grover.ui.plantinfo;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.grover.models.Plant;
import com.example.grover.models.PlantLogItem;
import com.example.grover.ui.plantinfo.rv.PlantLogAdapterRV;
import com.example.grover.R;
import com.example.grover.data.HomeRepository;
import com.example.grover.ui.addplant.AddPlantFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PlantInfoFragment extends Fragment implements PlantLogAdapterRV.OnListItemLongClickListener, AdapterView.OnItemSelectedListener {
    private PlantInfoViewModel viewModel;
    RecyclerView recyclerView;
    PlantLogAdapterRV plantLogAdapterRV;
    final Calendar myCalendar = Calendar.getInstance();
    private Spinner spin;
    ArrayList<String> journalTypes;
    EditText journalDate;
    Plant p;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(PlantInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_plant_info, container, false);

        journalTypes = new ArrayList<>();
        journalTypes.add("Repotting");
        journalTypes.add("Fertilizer");
        journalTypes.add("Water");
        journalTypes.add("Other");

        String plantId = this.getArguments().getString("PlantId");
        p = HomeRepository.getInstance().getHome().getValue().getPlantById(plantId);
        p.closeAllLogItems();

        recyclerView = root.findViewById(R.id.rv);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            plantLogAdapterRV = new PlantLogAdapterRV(p.getLog(), this, p.getPlantId());
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

            EditText notesJournal = root.findViewById(R.id.editTextTextMultiLine);

            Button addJournalButton = root.findViewById(R.id.button3);
            Button cancelJournalButton = root.findViewById(R.id.button4);

            journalDate = root.findViewById(R.id.journalDate);

            CardView trefleCard = root.findViewById(R.id.cardView6);
            CardView notesCard = root.findViewById(R.id.cardViewNotes);
            CardView currentCardView = root.findViewById(R.id.cardView3);
            CardView waterButton = root.findViewById(R.id.waterButton);
            CardView editButton = root.findViewById(R.id.editButton);
            CardView addJournalEntry = root.findViewById(R.id.addJournalEntry);
            CardView addJournalEntryContainer = root.findViewById(R.id.addJournalEntryContainer);

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

            addJournalEntryContainer.setVisibility(View.GONE);

            waterButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    viewModel.getHome().getValue().getPlantById(p.getPlantId()).water();
                    viewModel.updateHome(viewModel.getHome().getValue());
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
                    plantLogAdapterRV.setNewDataSet(p.getLog());
                    recyclerView.setAdapter(plantLogAdapterRV);
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
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        spin = (Spinner) root.findViewById(R.id.spinner2);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the journal types
        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,journalTypes);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        journalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addJournalEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addJournalEntryContainer.getVisibility() == View.GONE)
                    addJournalEntryContainer.setVisibility(View.VISIBLE);
                else
                    addJournalEntryContainer.setVisibility(View.GONE);
            }
        });

        cancelJournalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJournalEntryContainer.setVisibility(View.GONE);
            }
        });

        addJournalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( TextUtils.isEmpty(journalDate.getText())){
                    Toast.makeText(getContext(), "Specify a date", Toast.LENGTH_SHORT).show();
                    journalDate.setError( "A date is required!" );

                }else {
                    PlantLogItem logItem = new PlantLogItem(spin.getSelectedItem().toString());
                    logItem.setDate(myCalendar.getTime());
                    logItem.setNote(notesJournal.getText().toString());
                    //viewModel.getHome().getValue().getPlantById(p.getPlantId()).log(logItem);

                    p.log(logItem);
                    viewModel.updateHome(viewModel.getHome().getValue());

                    plantLogAdapterRV.setNewDataSet(p.getLog());
                    recyclerView.setAdapter(plantLogAdapterRV);
                    addJournalEntryContainer.setVisibility(View.GONE);
                }
            }
        });
        return root;
    }


    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMAN);

        journalDate.setText(sdf.format(myCalendar.getTime()));
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(PlantInfoViewModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onListItemLongClick(int clickedItemIndex) {
        p.longPressLog(clickedItemIndex);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
