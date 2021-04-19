package com.example.grover.models;

import android.net.Uri;
import android.util.Log;

import com.example.grover.data.ServiceGenerator;
import com.example.grover.data.TrefleApi;
import com.example.grover.models.trefle.trefleSpeciesComplete.Data;
import com.example.grover.models.trefle.trefleSpeciesComplete.Root;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Plant {
    //Done
    private String name;
    private String latinName;
    private int waterLevel;
    private double daysBetweenWater;
    private String note;

    private int mIconId;
    private int roomId;
    private Uri imgUri;

    //Don't need
    private String lastWaterDate;
    private String lastWaterDateUndo;
    private SimpleDateFormat sdf;
    private ArrayList<PlantLogItem> log;
    private int trefleId;
    private Data treflePlantInfo;

    public Plant(String name, String latinName, int mIconId, double daysBetweenWater, String lastWater, int waterLevel, int trefleId, int roomId, String note) {
        this.name = name;
        this.latinName = latinName;
        this.mIconId = mIconId;
        this.daysBetweenWater = daysBetweenWater;
        this.lastWaterDate = lastWater;
        lastWaterDateUndo = lastWater;
        this.waterLevel = waterLevel;
        this.roomId = roomId;
        this.trefleId = trefleId;
        this.note = note;
        log = new ArrayList<>();
        sdf= new SimpleDateFormat("dd-M-yyyy");
        treflePlantInfo = getTreflePlantInfo();
    }
    public Plant(){
        this.name = null;
        this.latinName = null;
        this.mIconId = -1;
        this.daysBetweenWater = -1;
        this.waterLevel = -1;
        this.roomId = -1;
        note = null;
        this.trefleId = 0;
        log = new ArrayList<>();
        sdf= new SimpleDateFormat("dd-M-yyyy");
        lastWaterDate = sdf.format(new Date());
        lastWaterDateUndo = sdf.format(new Date());
        treflePlantInfo = getTreflePlantInfo();
    }

    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getTrefleId() {
        return trefleId;
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

    public Data getTreflePlantInfo() {
        if (trefleId == 0)
            return null;
        if (treflePlantInfo == null){
            Log.d("Trefle","get " + name);
            TrefleApi trefleApi = ServiceGenerator.getTrefleApi();
            Call<Root> call = trefleApi.getDataFromId(getTrefleId());
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {
                    if (response.code() == 200) {
                        setTreflePlantInfo(response.body().data);
                        Log.d("Trefle","get " + name + " success");
                    }
                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {
                    Log.i("Retrofit", "Something went wrong :(");
                }
            });
        }
        return treflePlantInfo;
    }

    public void setTreflePlantInfo(Data treflePlantInfo) {
        this.treflePlantInfo = treflePlantInfo;
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

    public String getName() {
        return name;
    }

    public String getLatinName() {
        return latinName;
    }

    public int getmIconId() {
        return mIconId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
    }

    public void setDaysBetweenWater(double daysBetweenWater) {
        this.daysBetweenWater = daysBetweenWater;
    }

    public void setmIconId(int mIconId) {
        this.mIconId = mIconId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setLastWaterDate(String lastWaterDate) {
        this.lastWaterDate = lastWaterDate;
    }

    public void setLastWaterDateUndo(String lastWaterDateUndo) {
        this.lastWaterDateUndo = lastWaterDateUndo;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public void setLog(ArrayList<PlantLogItem> log) {
        this.log = log;
    }

    public void setTrefleId(int trefleId) {
        this.trefleId = trefleId;
        treflePlantInfo = getTreflePlantInfo();
    }
}
