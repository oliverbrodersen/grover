package com.example.grover;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.grover.Plant;

import java.util.ArrayList;

public class Home {
    private final int id;
    private ArrayList<Plant> plants;
    private ArrayList<Plant> plantsDisplayed;
    private ArrayList<Room> rooms;

    public Home(int id, ArrayList<Plant> plants) {
        this.id = id;
        this.plants = plants;
        plantsDisplayed = plants;
        rooms = new ArrayList<>();
    }

    public int getPlantIndex(Plant plant){
        return plants.indexOf(plant);
    }

    public int getId() {
        return id;
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }

    public ArrayList<Plant> getPlantsDisplayed() {
        return plantsDisplayed;
    }

    public String getPlantStatus(){
        int ptw = plantsToWater();
        if(ptw == 0)
            return "Your plants are looking good";
        return "You have " + ptw + " plant" + (ptw>1?"s":"") + " that needs water!";
    }
    public int plantsToWater(){
        int i = 0;
        for (Plant plant:plants) {
            if (plant.waterToday()<=0)
                i++;
        }
        return i;
    }
    public void addRoom(String name){
        Room room = new Room(name,Integer.parseInt(id + "" + rooms.size()) , id);
        rooms.add(room);
    }
    public void addRoom(String name, int roomId){
        Room room = new Room(name, roomId , id);
        rooms.add(room);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Plant> getPlantsByWaterNeed(){
        plantsDisplayed = plants;
        plantsDisplayed.sort(new CompareWaterBy());
        return plantsDisplayed;
    }

    public String getRoomById(int roomId) {
        for (Room r:
             rooms) {
            if (r.getRoomId() == roomId)
                return r.getName();
        }
        return "";
    }
}
