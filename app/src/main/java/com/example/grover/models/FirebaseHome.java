package com.example.grover.models;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHome {
    private List<FirebasePlant> plants;
    private List<Room> rooms;

    public FirebaseHome(List<FirebasePlant> plants, List<Room> rooms) {
        this.plants = plants;
        this.rooms = rooms;
    }

    public FirebaseHome() {
        plants = new ArrayList<>();
        rooms = new ArrayList<>();
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
