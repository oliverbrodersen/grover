package com.example.grover.models;

public class Room {
    private String name;
    private int roomId;

    public Room() {
    }

    public Room(String name, int roomId) {
        this.name = name;
        this.roomId = roomId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }


    public String getName() {
        return name;
    }

    public int getRoomId() {
        return roomId;
    }
}

