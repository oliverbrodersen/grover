package com.example.grover;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Plant {
    private String name;
    private String latinName;
    private int waterLevel;
    private int mIconId;
    private int roomId;
    private boolean favorite;
    private double daysBetweenWater;
    private String lastWaterDate;
    private String lastWaterDateUndo;
    private SimpleDateFormat sdf;
    private ArrayList<PlantLogItem> log;

    public Plant(String name, String latinName, int mIconId, boolean favorite, double daysBetweenWater, String lastWaterMS, int waterLevel, int roomId) {
        this.name = name;
        this.latinName = latinName;
        this.mIconId = mIconId;
        this.favorite = favorite;
        this.daysBetweenWater = daysBetweenWater;
        this.lastWaterDate = lastWaterMS;
        lastWaterDateUndo = lastWaterMS;
        this.waterLevel = waterLevel;
        this.roomId = roomId;
        log = new ArrayList<>();
        sdf= new SimpleDateFormat("dd-M-yyyy");
    }

    public String getLastWaterDate() {
        return lastWaterDate;
    }

    public String getLastWaterDateUndo() {
        return lastWaterDateUndo;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    public void log(String type){
        log.add(new PlantLogItem(type));
    }
    public void log(PlantLogItem plantLogItem){
        log.add(plantLogItem);
    }
    public ArrayList<PlantLogItem> getLog() {
        return log;
    }

    public Date getLastWaterAsDate(){
        Date d = null;
        try{
            d = sdf.parse(lastWaterDate);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return d;
    }
    public String waterWhen(){
        int waterIn = (int) daysBetweenWater - getDaysSinceLastWater();
        if (waterIn < -1)
            return (waterIn * -1) + " days late!";
        else
            switch (waterIn){
                case -1: return "1 day late!";
                case 0: return "Water today";
                case 1: return "Water tomorrow";
                default: return "Water in " + waterIn + " days";
            }
    }
    public int getDaysSinceLastWater(){
        Date d = new Date();
        long diff = d.getTime() - getLastWaterAsDate().getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
    //return positive integer if not yet, 0 if today and negative integer if overdue
    public int waterToday(){
        return (int) daysBetweenWater - getDaysSinceLastWater();
    }
    public void water(){
        lastWaterDateUndo = lastWaterDate;
        lastWaterDate = sdf.format(new Date());
        log("Water");
    }

    public void undoWater(){
        lastWaterDate = lastWaterDateUndo;
        log.remove(log.size()-1);
    }

    public double getDaysBetweenWater() {
        return daysBetweenWater;
    }

    public String getlastWaterDate() {
        return lastWaterDate;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setAsFavorite(){
        favorite = true;
    }
    public void removeAsFavorite(){
        favorite = false;
    }
    public boolean toogleFavorite(){
        favorite = !favorite;
        return favorite;
    }
    public String getName() {
        return name;
    }

    public String getLatinName() {
        return latinName;
    }

    public int getmIconId() {
        return mIconId;
    }
}
