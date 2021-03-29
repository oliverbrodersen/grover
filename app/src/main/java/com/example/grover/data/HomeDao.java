package com.example.grover.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.grover.Home;
import com.example.grover.Plant;
import com.example.grover.R;

import java.util.ArrayList;

public class HomeDao {
    private MutableLiveData<Home> home;
    private static HomeDao instance;

    private HomeDao(){
        home = new MutableLiveData<>();

        ArrayList<Plant> plants = new ArrayList<>();
        Plant p1 = new Plant("Fredslilje", "Spathiphyllum", R.drawable.p2, false, 5, "27-03-2021", 3, 0);
        p1.log("Water");
        p1.log("Water");
        p1.log("Fertilizer");
        p1.log("Water");
        p1.log("Fertilizer");
        p1.log("Repotting");
        p1.log("Water");
        p1.log("Water");
        plants.add(p1);
        plants.add(new Plant("Philodendron","Philodendron Red Beauty", R.drawable.p1, true, 14, "29-03-2021", 3, 0));
        plants.add(new Plant("Cocospalme", "Cocos nucifera", R.drawable.p3, true, 8, "26-03-2021", 1,1));
        plants.add(new Plant("Banantræ", "Bananus fantomium", R.drawable.p4, false, 15,"20-03-2021", 1,0));
        plants.add(new Plant("Trailing Jade", "Bananus fantomium", R.drawable.p5, false, 15,"20-03-2021", 2,0));
        plants.add(new Plant("Nerve plante", "Fittonia", R.drawable.p6, false, 5,"21-03-2021", 2,0));
        plants.add(new Plant("Guldranke", "Epipremnum Aureum", R.drawable.p7, true, 10,"29-03-2021", 2,0));

        Home _home = new Home(420, plants);
        _home.addRoom("Living room", 0);
        _home.addRoom("Kitchen", 1);
        home.setValue(_home);
    }

    public static HomeDao getInstance(){
        if (instance == null)
            instance = new HomeDao();
        return instance;
    }
    public LiveData<Home> getHome(){
        return home;
    }

    public void updateHome(Home home) {
        this.home.setValue(home);
    }
}
