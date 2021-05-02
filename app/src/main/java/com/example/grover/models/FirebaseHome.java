package com.example.grover.models;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHome {
    private int homeId;
    private List<FirebasePlant> plants;
    private List<Room> rooms;

    public FirebaseHome(int homeId, List<FirebasePlant> plants, List<Room> rooms) {
        this.homeId = homeId;
        this.plants = plants;
        this.rooms = rooms;
    }

    public FirebaseHome() {
        plants = new ArrayList<>();
        rooms = new ArrayList<>();
    }

    public int getHomeId() {
        return homeId;
    }

    public void setHomeId(int homeId) {
        this.homeId = homeId;
    }

    public List<FirebasePlant> getPlants() {
        return plants;
    }

    public void setPlants(List<FirebasePlant> plants) {
        this.plants = plants;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
