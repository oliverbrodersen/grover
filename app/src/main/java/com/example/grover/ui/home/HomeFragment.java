package com.example.grover.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
import com.example.grover.R;
import com.example.grover.ui.Home;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment implements PlantAdapterRV.OnListItemClickListener, PlantAdapterRV.OnListItemLongClickListener {

    private HomeViewModel homeViewModel;
    RecyclerView mPlantList;
    PlantAdapterRV mPlantAdapter;
    Home home;
    TextView plantStatus;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mPlantList = root.findViewById(R.id.rv);
        mPlantList.hasFixedSize();
        mPlantList.setLayoutManager(new GridLayoutManager(getContext(), 2));

        ArrayList<Plant> plants = new ArrayList<>();
        plants.add(new Plant("Philodendron","Philodendron Red Beauty", R.drawable.p1, true, 14, "14-03-2021"));
        plants.add(new Plant("Fredslilje", "Spathiphyllum", R.drawable.p2, false, 5, "13-03-2021"));
        plants.add(new Plant("Cocospalme", "Cocos nucifera", R.drawable.p3, true, 8, "07-03-2021"));
        plants.add(new Plant("Banantræ", "Bananus fantomium", R.drawable.p4, false, 15,"14-03-2021"));

        plants.add(new Plant("Trailing Jade", "Peperomia rotundifolia", R.drawable.p5, false, 10,"14-03-2021"));
        plants.add(new Plant("Nerve plante", "Fittonia", R.drawable.p6, false, 7,"14-03-2021"));
        plants.add(new Plant("Guldranke", "Epipremnum Aureum", R.drawable.p7, true, 10,"04-03-2021"));

        home = new Home(420, plants);
        mPlantAdapter = new PlantAdapterRV(plants, this, this);
        mPlantList.setAdapter(mPlantAdapter);

        plantStatus = root.findViewById(R.id.plantStatus);
        plantStatus.setText(home.getPlantStatus());

        return root;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Snackbar mSnackbar = Snackbar.make(getView(), home.getPlants().get(clickedItemIndex).getName(), Snackbar.LENGTH_SHORT);
        mSnackbar.show();
    }
    @Override
    public void onListItemLongClick(int clickedItemIndex) {
        home.getPlants().get(clickedItemIndex).water();
        plantStatus.setText(home.getPlantStatus());
        //update view;
        mPlantAdapter.notifyDataSetChanged();

        Snackbar snackbar = Snackbar
                .make(getView(), "Watered " + home.getPlants().get(clickedItemIndex).getName(), Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        home.getPlants().get(clickedItemIndex).undoWater();
                        plantStatus.setText(home.getPlantStatus());
                        mPlantAdapter.notifyDataSetChanged();

                        Snackbar mSnackbar = Snackbar.make(getView(), "Watering has been undone", Snackbar.LENGTH_SHORT);
                        mSnackbar.show();
                    }
                });

        snackbar.show();
    }
}