package com.example.grover.models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class FirebasePlant {
    private String id, name, latinName, note, lastWaterDate, imageId;
    private int waterLevel, daysBetweenWater, roomId, trefleId;
    private List<PlantLogItem> log;

    public FirebasePlant() {
        log = new ArrayList<>();
    }

    public FirebasePlant(String id, String name, String latinName, String note, String lastWaterDate, int waterLevel, int daysBetweenWater, int roomId, int trefleId, List<PlantLogItem> log, String imageId) {
        this.id = id;
        this.name = name;
        this.latinName = latinName;
        this.note = note;
        this.imageId = imageId;
        this.lastWaterDate = lastWaterDate;
        this.waterLevel = waterLevel;
        this.daysBetweenWater = daysBetweenWater;
        this.roomId = roomId;
        this.trefleId = trefleId;
        this.log = log;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLastWaterDate() {
        return lastWaterDate;
    }

    public void setLastWaterDate(String lastWaterDate) {
        this.lastWaterDate = lastWaterDate;
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
    }

    public int getDaysBetweenWater() {
        return daysBetweenWater;
    }

    public void setDaysBetweenWater(int daysBetweenWater) {
        this.daysBetweenWater = daysBetweenWater;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getTrefleId() {
        return trefleId;
    }

    public void setTrefleId(int trefleId) {
        this.trefleId = trefleId;
    }

    public List<PlantLogItem> getLog() {
        return log;
    }

    public void setLog(List<PlantLogItem> log) {
        this.log = log;
    }
}
