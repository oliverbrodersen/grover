package com.example.grover.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grover.Plant;
import com.example.grover.PlantAdapterRV;
import com.example.grover.R;
import com.example.grover.Home;
import com.example.grover.ui.plantinfo.PlantInfoActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements PlantAdapterRV.OnListItemClickListener, PlantAdapterRV.OnListItemLongClickListener {

    private HomeViewModel viewModel;
    RecyclerView mPlantList;
    PlantAdapterRV mPlantAdapter;
    TextView plantStatus;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mPlantList = root.findViewById(R.id.rv);
        mPlantList.hasFixedSize();
        mPlantList.setLayoutManager(new GridLayoutManager(getContext(), 2));


        mPlantAdapter = new PlantAdapterRV(viewModel.getHome().getValue().getPlantsByWaterNeed(), this, this);
        mPlantList.setAdapter(mPlantAdapter);

        plantStatus = root.findViewById(R.id.plantStatus);
        plantStatus.setText(viewModel.getHome().getValue().getPlantStatus());

        return root;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Context context = getContext();
        Class destination = PlantInfoActivity.class;

        Intent intent = new Intent(context, destination);

        intent.putExtra("PlantId", clickedItemIndex);
        startActivity(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onListItemLongClick(int clickedItemIndex) {
        Plant p = viewModel.getHome().getValue().getPlantsDisplayed().get(clickedItemIndex);
        int pIndex = viewModel.getHome().getValue().getPlantIndex(p);

        viewModel.getHome().getValue().getPlants().get(pIndex).water();

        plantStatus.setText(viewModel.getHome().getValue().getPlantStatus());

        Snackbar snackbar = Snackbar
                .make(getView(), "Watered " + viewModel.getHome().getValue().getPlants().get(pIndex).getName(), Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pIndex2 = viewModel.getHome().getValue().getPlantIndex(p);
                        viewModel.getHome().getValue().getPlants().get(pIndex2).undoWater();

                        plantStatus.setText(viewModel.getHome().getValue().getPlantStatus());
                        mPlantAdapter.setNewDataSet(viewModel.getHome().getValue().getPlantsByWaterNeed());
                        mPlantAdapter.notifyDataSetChanged();

                        Snackbar mSnackbar = Snackbar.make(getView(), p.getName() + " has been dehydrated", Snackbar.LENGTH_SHORT);
                        mSnackbar.show();
                    }
                });

        snackbar.show();
        mPlantAdapter.setNewDataSet(viewModel.getHome().getValue().getPlantsByWaterNeed());
        //update view;
        mPlantAdapter.notifyDataSetChanged();
    }
}