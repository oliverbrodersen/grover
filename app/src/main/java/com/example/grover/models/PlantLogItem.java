package com.example.grover.models;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlantLogItem {
    private String type;
    private String color;
    private Date date;
    private String note = null;

    private MutableLiveData<Boolean> longpressed;

    private SimpleDateFormat sdf;

    public PlantLogItem() {
        sdf= new SimpleDateFormat("dd/M/yyyy");
        longpressed = new MutableLiveData<>();
        longpressed.setValue(false);
    }

    public PlantLogItem(String type) {
        this.type = type;
        date = new Date();
        sdf= new SimpleDateFormat("dd/M/yyyy");
        longpressed = new MutableLiveData<>();
        longpressed.setValue(false);
        switch(type){
            case "Water": color = "#afe3ff"; break;
            case "Fertilizer": color = "#58dc71"; break;
            case "Repotting": color = "#b98658"; break;
            default: color = "#588157";
        }
    }

    public PlantLogItem(String type, Date date, String note) {
        this.type = type;
        this.date = date;
        this.note = note;
        sdf= new SimpleDateFormat("dd/M/yyyy");
        longpressed = new MutableLiveData<>();
        longpressed.setValue(false);
        switch(type){
            case "Water": color = "#afe3ff"; break;
            case "Fertilizer": color = "#58dc71"; break;
            case "Repotting": color = "#b98658"; break;
            default: color = "#588157";
        }
    }

    @Exclude
    public boolean getLongpressedAsBoolean() {
        return longpressed.getValue();
    }

    @Exclude
    public MutableLiveData<Boolean> getLongpressed(){
        return longpressed;
    }

    @Exclude
    public void setLongpressed(MutableLiveData<Boolean> longpressed) {
        this.longpressed = longpressed;
    }

    @Exclude
    public void setLongpressedAsBoolean(boolean longpressed) {
        this.longpressed.setValue(longpressed);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return sdf.format(date) + " - " + type;
    }

    public String getNote() {
        return note;
    }
}
