package com.example.grover.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grover.data.HomeRepository;
import com.example.grover.models.Home;
import com.example.grover.models.Plant;
import com.example.grover.models.PlantAdapterRV;
import com.example.grover.R;
import com.example.grover.ui.plantinfo.PlantInfoFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements PlantAdapterRV.OnListItemClickListener, PlantAdapterRV.OnListItemLongClickListener {

    private HomeViewModel viewModel;
    RecyclerView mPlantList;
    PlantAdapterRV mPlantAdapter;
    ImageView headerColor;
    TextView plantStatus;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        plantStatus = root.findViewById(R.id.plantStatus);
        headerColor = root.findViewById(R.id.imageView4);
        mPlantList = root.findViewById(R.id.rv);
        mPlantList.hasFixedSize();
        mPlantList.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mPlantAdapter = new PlantAdapterRV(new ArrayList<Plant>(),this, this);

        viewModel.getHome().observe(getViewLifecycleOwner(), new Observer<Home>() {
            @Override
            public void onChanged(Home home) {
                mPlantAdapter.setNewDataSet(viewModel.getHome().getValue().getPlantsByWaterNeed());
                mPlantList.setAdapter(mPlantAdapter);

                plantStatus.setText(viewModel.getHome().getValue().getPlantStatus());
            }
        });

        return root;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        FragmentTransaction fragmentTransaction = getActivity()
                .getSupportFragmentManager().beginTransaction();
        PlantInfoFragment fragment = new PlantInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("PlantId", HomeRepository.getInstance().getHome().getValue().getPlantsDisplayed().get(clickedItemIndex).getPlantId());
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.addToBackStack( "tag" );
        fragmentTransaction.commit();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onListItemLongClick(int clickedItemIndex) {
        Plant p = viewModel.getHome().getValue().getPlantsDisplayed().get(clickedItemIndex);
        String lastWater = p.getLastWaterDate();

        viewModel.getHome().getValue().getPlantById(p.getPlantId()).water();
        viewModel.updateDatabase();

        plantStatus.setText(viewModel.getHome().getValue().getPlantStatus());

        Snackbar snackbar = Snackbar
                .make(getView(), "Watered " + viewModel.getHome().getValue().getPlantById(p.getPlantId()).getName(), Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewModel.getHome().getValue().getPlantById(p.getPlantId()).undoWater(lastWater);
                        viewModel.updateDatabase();

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