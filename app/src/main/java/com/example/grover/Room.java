package com.example.grover;

public class Room {
    private String name;
    private int roomId;
    private int homeId;

    public Room(String name, int roomId, int homeId) {
        this.name = name;
        this.roomId = roomId;
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

