package com.example.grover.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grover.data.HomeRepository;
import com.example.grover.models.Home;
import com.example.grover.models.Plant;
import com.example.grover.ui.home.rv.PlantAdapterRV;
import com.example.grover.R;
import com.example.grover.ui.plantinfo.PlantInfoFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements PlantAdapterRV.OnListItemClickListener, PlantAdapterRV.OnListItemLongClickListener {

    private HomeViewModel viewModel;
    private String HomeId;
    private String HomeOwner;
    RecyclerView mPlantList;
    PlantAdapterRV mPlantAdapter;
    ImageView headerColor;
    TextView plantStatus;
    TextView ownerName;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        if (this.getArguments() != null){
            HomeId = this.getArguments().getString("HomeId");
            HomeOwner = this.getArguments().getString("HomeOwner");
        }

        plantStatus = root.findViewById(R.id.plantStatus);
        headerColor = root.findViewById(R.id.imageView4);

        mPlantList = root.findViewById(R.id.rv);
        mPlantList.hasFixedSize();
        mPlantList.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mPlantAdapter = new PlantAdapterRV(new ArrayList<Plant>(),this, this);

        if (HomeId == null){
            viewModel.getHome().observe(getViewLifecycleOwner(), new Observer<Home>() {
                @Override
                public void onChanged(Home home) {
                    mPlantAdapter.setNewDataSet(viewModel.getHome().getValue().getPlantsByWaterNeed());
                    mPlantList.setAdapter(mPlantAdapter);
                    plantStatus.setText(viewModel.getHome().getValue().getPlantStatus());
                }
            });
        }
        else{
            viewModel.getHome(HomeId).observe(getViewLifecycleOwner(), new Observer<Home>() {
                @Override
                public void onChanged(Home home) {
                    mPlantAdapter.setNewDataSet(viewModel.getFriendHome().getValue().getPlantsByWaterNeed());
                    mPlantAdapter.setOwner(viewModel.getFriend(HomeId));
                    mPlantList.setAdapter(mPlantAdapter);
                    plantStatus.setText(viewModel.getFriendHome().getValue().getPlantStatus(HomeOwner));
                }
            });
        }

        return root;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (HomeId == null){
            PlantInfoFragment fragment = new PlantInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putString("PlantId", HomeRepository.getInstance().getHome().getValue().getPlantsDisplayed().get(clickedItemIndex).getPlantId());
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).addToBackStack("tag").commit();
        }
        else
            Toast.makeText(getContext(), "You do not have permission to see " + HomeOwner + "'s plants", Toast.LENGTH_SHORT).show();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onListItemLongClick(int clickedItemIndex) {
        if (HomeId == null){
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
        else
            Toast.makeText(getContext(), "You do not have permission to water " + HomeOwner + "'s plants", Toast.LENGTH_SHORT).show();
    }
}