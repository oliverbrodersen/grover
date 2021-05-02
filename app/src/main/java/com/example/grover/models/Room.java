package com.example.grover.models;

public class Room {
    private String name;
    private int roomId;
    private int homeId;

    public Room() {
    }

    public Room(String name, int roomId, int homeId) {
        this.name = name;
        this.roomId = roomId;
        this.homeId = homeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setHomeId(int homeId) {
        this.homeId = homeId;
    }

    public String getName() {
        return name;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getHomeId() {
        return homeId;
    }
}

