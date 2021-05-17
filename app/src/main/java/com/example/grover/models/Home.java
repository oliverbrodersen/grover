package com.example.grover.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class Home {
    private ArrayList<Plant> plants;
    private ArrayList<Plant> plantsDisplayed;
    private ArrayList<Room> rooms;

    public Home(FirebaseHome ref){
        rooms = (ArrayList<Room>) ref.getRooms();
        plants = new ArrayList<>();
        for (FirebasePlant firebasePlant : ref.getPlants()){
            plants.add(new Plant(firebasePlant));
        }
        plantsDisplayed = plants;
    }

    public Home(ArrayList<Plant> plants) {
        this.plants = plants;
        plantsDisplayed = plants;
        rooms = new ArrayList<>();
    }

    public int getPlantIndex(Plant plant){
        return plants.indexOf(plant);
    }


    public ArrayList<Plant> getPlants() {
        return plants;
    }

    public ArrayList<Plant> getPlantsDisplayed() {
        return plantsDisplayed;
    }

    public FirebaseHome getAsFirebaseHome(){
        FirebaseHome firebaseHome = new FirebaseHome();
        ArrayList<FirebasePlant> firebasePlantArrayList = new ArrayList<>();
        for(Plant plant : plants){
            firebasePlantArrayList.add(plant.getAsFirebasePlant());
        }
        firebaseHome.setRooms(rooms);
        firebaseHome.setPlants(firebasePlantArrayList);
        return firebaseHome;
    }

    public String getPlantStatus(){
        int ptw = plantsToWater();
        if(ptw == 0)
            return "Your plants are looking good";
        return "You have " + ptw + " plant" + (ptw>1?"s":"") + " that needs water!";
    }

    public String getPlantStatus(String homeOwner) {
        int ptw = plantsToWater();
        if(ptw == 0)
            return homeOwner + "'s plants are looking good";
        return homeOwner + " have " + ptw + " plant" + (ptw>1?"s":"") + " that needs water!";
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
        Room room = new Room(name,rooms.size() + 1);
        rooms.add(room);
    }
    public void addRoom(String name, int roomId){
        Room room = new Room(name, roomId);
        rooms.add(room);
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public int getPositionOfRoom(int id){
        int position = 0;
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomId() == id)
                return position;
            position++;
        }
        return 0;
    }
    public void addPlant(Plant plant){
        plants.add(plant);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Plant> getPlantsByWaterNeed(){
        plantsDisplayed = plants;
        plantsDisplayed.sort(new CompareWaterBy());
        return plantsDisplayed;
    }

    public void setPlants(ArrayList<Plant> plants) {
        this.plants = plants;
    }

    public Plant getPlantByName(String name){
        for (Plant p:plants) {
            if (p.getName().equals(name))
                return p;
        }
        return null;
    }
    public String getRoomById(int roomId) {
        for (Room r:
             rooms) {
            if (r.getRoomId() == roomId)
                return r.getName();
        }
        return "";
    }

    public ArrayList<String> getRoomsList() {
        ArrayList<String> roomsStrings = new ArrayList<>();
        for (Room r:rooms) {
            roomsStrings.add(r.getName());
        }
        roomsStrings.add("Other");
        return roomsStrings;
    }

    public Plant getPlantById(String plantId) {
        for (Plant plant : plants){
            if (plant.getPlantId().equals(plantId))
                return plant;
        }
        return null;
    }

    public void editPlant(Plant newPlant, Plant oldPlant) {
        getPlantById(newPlant.getPlantId()).update(oldPlant);
    }

}
