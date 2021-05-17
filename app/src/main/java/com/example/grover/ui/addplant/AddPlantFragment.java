package com.example.grover.ui.addplant;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.grover.R;
import com.example.grover.data.HomeRepository;
import com.example.grover.models.Home;
import com.example.grover.models.Plant;
import com.example.grover.ui.addplant.rv.TrefleSearchAdapterRV;
import com.example.grover.models.trefle.TrefleSearchQueryStripped;
import com.example.grover.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class AddPlantFragment extends Fragment implements TrefleSearchAdapterRV.OnTrefleResultsItemClickListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private AddPlantViewModel galleryViewModel;
    final Calendar myCalendar = Calendar.getInstance();
    private StorageReference mStorageRef;
    private boolean edit;
    private String imageId;
    private Plant newPlant, oldPlant;
    private EditText edittext, name, species, daysBetweenWater, notes, etOtherLocation;
    private Slider waterAmount;
    private ImageView pPhotoView;
    private CardView cardLoad;
    private TextView tvOtherLocation, photoError;
    private Button createButton, bOtherLocation, deleteButton;
    private RecyclerView trefleSearchList;
    private Spinner spin;
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
        deleteButton = root.findViewById(R.id.deleteButton);
        tvOtherLocation = root.findViewById(R.id.textView106);
        photoError = root.findViewById(R.id.photoError);
        etOtherLocation = root.findViewById(R.id.editTextOtherLocation);
        bOtherLocation = root.findViewById(R.id.buttonOtherLocation);
        pPhotoView = (ImageView) root.findViewById(R.id.imageView8);
        cardLoad = (CardView) root.findViewById(R.id.cardView2);
        edit = false;
            imageId = UUID.randomUUID().toString() + ".jpg";

        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        spin = (Spinner) root.findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the room list
        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,galleryViewModel.getHome().getValue().getRoomsList());
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        String plantId = null;
        if (this.getArguments() != null) {
            edit = true;
            plantId = this.getArguments().getString("PlantId");
            newPlant = HomeRepository.getInstance().getHome().getValue().getPlantById(plantId);
            oldPlant = newPlant;
            Log.d("test", newPlant.getName());

            //Download and view image
            StorageReference ref = FirebaseStorage.getInstance().getReference();
            ref.child("images/" + newPlant.getImageId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    //viewHolder.icon.setImageURI(uri);
                    Glide.with(getContext())
                            .load(uri)
                            .into(pPhotoView);
                    photoError.setVisibility(View.INVISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Log.e("tag", exception.toString());
                }
            });

            name.setText(newPlant.getName());
            species.setText(newPlant.getLatinName());
            daysBetweenWater.setText("" + (int) newPlant.getDaysBetweenWater());
            waterAmount.setValue(newPlant.getWaterLevel());
            notes.setText(newPlant.getNote());
            createButton.setText("Save");
            spin.setSelection(galleryViewModel.getHome().getValue().getPositionOfRoom(newPlant.getRoomId()));
        }
        else {
            newPlant = new Plant();
            newPlant.setPlantId(UUID.randomUUID().toString());
            createButton.setText("Create");
        }

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
                Boolean check = false;
                closeKeyboard();
                if( TextUtils.isEmpty(name.getText())){
                    check = true;
                    name.setError( "Name is required!" );
                }
                if( TextUtils.isEmpty(daysBetweenWater.getText())){
                    check = true;
                    daysBetweenWater.setError( "Days between watering is required!" );
                }
                if( newPlant.getImageId() == null){
                    check = true;
                    photoError.setVisibility(View.VISIBLE);
                }
                if (check){
                    Toast.makeText(getContext(), "Please fill out required fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    newPlant.setName(name.getText().toString());
                    newPlant.setLatinName(species.getText().toString());
                    newPlant.setDaysBetweenWater(Integer.parseInt(String.valueOf(daysBetweenWater.getText())));
                    newPlant.setWaterLevel((int) waterAmount.getValue());
                    newPlant.setNote(notes.getText().toString());
                    if (!edit) {
                        newPlant.setTrefleId(trefleIdPlaceholder);
                        uploadImage();
                    }
                    galleryViewModel.addPlant(newPlant, oldPlant);

                    FragmentTransaction fragmentTransaction = getActivity()
                            .getSupportFragmentManager().beginTransaction();
                    HomeFragment fragment = new HomeFragment();
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                    fragmentTransaction.commit();
                }
            }
        });

        //Only allow photo selection on creation
        if (!edit){
            pPhotoView.setOnClickListener(this);
            cardLoad.setCardBackgroundColor(getResources().getColor(R.color.image_load_idle));
            deleteButton.setVisibility(View.GONE);
        }
        else{
            cardLoad.setCardBackgroundColor(getResources().getColor(R.color.image_load_uploaded));
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    //builder.setMessage("Are you sure you want to permanently delete this plant?")
                    //        .setCancelable(false)
                    //        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    //            public void onClick(DialogInterface dialog, int id) {
                    //                galleryViewModel.deletePlant(newPlant.getPlantId());
                    //                Toast.makeText(getContext(), "Plant successfully deleted", Toast.LENGTH_SHORT).show();
                    //                FragmentTransaction fragmentTransaction = getActivity()
                    //                        .getSupportFragmentManager().beginTransaction();
                    //                HomeFragment fragment = new HomeFragment();
                    //                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                    //                fragmentTransaction.commit();
                    //            }
                    //        })
                    //        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    //            public void onClick(DialogInterface dialog, int id) {
                    //                dialog.cancel();
                    //            }
                    //        });
                    //AlertDialog alert = builder.create();

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    galleryViewModel.deletePlant(newPlant.getPlantId());
                                    Toast.makeText(getContext(), "Plant successfully deleted", Toast.LENGTH_SHORT).show();
                                    FragmentTransaction fragmentTransaction = getActivity()
                                            .getSupportFragmentManager().beginTransaction();
                                    HomeFragment fragment = new HomeFragment();
                                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                                    fragmentTransaction.commit();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Are you sure you want to permanently delete this plant?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            });
            deleteButton.setVisibility(View.VISIBLE);
        }



        //Trefle lookup init
        trefleSearchList = root.findViewById(R.id.rvTrefle);
        trefleSearchList.hasFixedSize();
        trefleSearchList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        trefleSearchAdapter = new TrefleSearchAdapterRV(this);
        trefleSearchList.setAdapter(trefleSearchAdapter);

        galleryViewModel.clearSearch();

        //Listen to incomming searches
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

        tvOtherLocation.setVisibility(View.GONE);
        etOtherLocation.setVisibility(View.GONE);
        bOtherLocation.setVisibility(View.GONE);

        bOtherLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                closeKeyboard();
                Home home = galleryViewModel.getHome().getValue();
                home.addRoom(etOtherLocation.getText().toString());
                galleryViewModel.updateHome(home);

                //Creating the ArrayAdapter instance having the country list
                ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,home.getRoomsList());
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spin.setAdapter(aa);

                spin.setSelection(home.getRooms().size()-1);

                newPlant.setRoomId(galleryViewModel.getHome().getValue().getRooms().get(galleryViewModel.getHome().getValue().getRooms().size()-1).getRoomId());
            }
        });
        return root;
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Uploading image...");
        pd.show();

        StorageReference riversRef = mStorageRef.child("images/" + imageId);
        riversRef.putFile(newPlant.getImageUri())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(getActivity().findViewById(R.id.nav_view), "Image uploaded", Snackbar.LENGTH_SHORT);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Snackbar.make(getActivity().findViewById(R.id.nav_view), "Image didn't upload", Snackbar.LENGTH_SHORT);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progressPercent = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setTitle("Progress: " + (int)progressPercent + "%");
                    }
            });
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
            newPlant.setImageId(imageId);
            pPhotoView.setImageURI(selectedImage);
            photoError.setVisibility(View.INVISIBLE);
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
        Log.d("grover", position + "");
        if (position == galleryViewModel.getHome().getValue().getRooms().size()){
            tvOtherLocation.setVisibility(View.VISIBLE);
            etOtherLocation.setVisibility(View.VISIBLE);
            bOtherLocation.setVisibility(View.VISIBLE);
        }
        else{
            tvOtherLocation.setVisibility(View.GONE);
            etOtherLocation.setVisibility(View.GONE);
             bOtherLocation.setVisibility(View.GONE);
            newPlant.setRoomId(galleryViewModel.getHome().getValue().getRooms().get(position).getRoomId());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void closeKeyboard()
    {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = getActivity().getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }
}