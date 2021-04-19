package com.example.grover.ui.addplant;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grover.R;
import com.example.grover.models.Plant;
import com.example.grover.models.TrefleSearchAdapterRV;
import com.example.grover.models.trefle.TrefleSearchQueryStripped;
import com.example.grover.ui.home.HomeFragment;
import com.example.grover.ui.plantinfo.PlantInfoFragment;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class AddPlantFragment extends Fragment implements TrefleSearchAdapterRV.OnTrefleResultsItemClickListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private AddPlantViewModel galleryViewModel;
    final Calendar myCalendar = Calendar.getInstance();
    private Plant newPlant;
    private EditText edittext, name, species, daysBetweenWater, waterAmount, notes;
    private ImageView pPhotoView;
    private CardView cardLoad;
    private Button createButton;
    private RecyclerView trefleSearchList;
    private TrefleSearchAdapterRV trefleSearchAdapter;
    private int trefleIdPlaceholder = 0;
    private static final int RESULT_CODE_IMAGE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(AddPlantViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_plant, container, false);
        edittext = (EditText) root.findViewById(R.id.Birthday);
        name = root.findViewById(R.id.editTextTextPersonName2);
        species = root.findViewById(R.id.editTextTextPersonName3);
        daysBetweenWater = root.findViewById(R.id.editTextNumberSigned);
        waterAmount = root.findViewById(R.id.editTextNumber);
        notes = root.findViewById(R.id.editTextTextMultiLine2);
        createButton = root.findViewById(R.id.button2);
        newPlant = new Plant();

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newPlant.setName(name.getText().toString());
                newPlant.setLatinName(species.getText().toString());
                newPlant.setDaysBetweenWater(Integer.parseInt(String.valueOf(daysBetweenWater.getText())));
                newPlant.setWaterLevel(Integer.parseInt(waterAmount.getText().toString()));
                newPlant.setNote(notes.getText().toString());
                newPlant.setTrefleId(trefleIdPlaceholder);
                galleryViewModel.addPlant(newPlant);
                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                HomeFragment fragment = new HomeFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.commit();
            }
        });
        pPhotoView = (ImageView) root.findViewById(R.id.imageView8);
        pPhotoView.setOnClickListener(this);
        cardLoad = (CardView) root.findViewById(R.id.cardView2);
        cardLoad.setCardBackgroundColor(getResources().getColor(R.color.image_load_idle));

        //Trefle lookup init
        trefleSearchList = root.findViewById(R.id.rvTrefle);
        trefleSearchList.hasFixedSize();
        trefleSearchList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        trefleSearchAdapter = new TrefleSearchAdapterRV(this);
        trefleSearchList.setAdapter(trefleSearchAdapter);

        galleryViewModel.clearSearch();

        galleryViewModel.getSearchResult().observe(getActivity(), new Observer<List<TrefleSearchQueryStripped>>() {
            @Override
            public void onChanged(List<TrefleSearchQueryStripped> trefleSearchQueryStrippeds) {
                trefleSearchAdapter.setNewDataSet(trefleSearchQueryStrippeds);
            }
        });

        //Run on every type
        species.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() > 2){
                    galleryViewModel.searchTrefle(s.toString());
                }
            }
        });

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) root.findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,galleryViewModel.getHome().getValue().getRoomsList());
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        return root;
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMAN);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    public void onClick(View v) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_CODE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            cardLoad.setCardBackgroundColor(getResources().getColor(R.color.image_load_uploaded));
            newPlant.setImgUri(selectedImage);
            pPhotoView.setImageURI(selectedImage);
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Log.d("Trefle", clickedItemIndex + "");
        TrefleSearchQueryStripped tp = galleryViewModel.selectSearchResult(clickedItemIndex);
        trefleIdPlaceholder = tp.id;
        newPlant.setLatinName(tp.species);
        species.setText(tp.species);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        newPlant.setRoomId(galleryViewModel.getHome().getValue().getRooms().get(position).getRoomId());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}