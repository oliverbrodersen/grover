package com.example.grover.ui;

import com.example.grover.Plant;

import java.util.ArrayList;

public class Home {
    private final int id;
    private ArrayList<Plant> plants;

    public Home(int id, ArrayList<Plant> plants) {
        this.id = id;
        this.plants = plants;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }

    public String getPlantStatus(){
        int ptw = plantsToWater();
        if(ptw == 0)
            return "Your plants are looking good";
        return "You have " + ptw + " plants that needs water!";
    }
    public int plantsToWater(){
        int i = 0;
        for (Plant plant:plants) {
            if (plant.waterToday()<=0)
                i++;
        }
        return i;
    }
}
