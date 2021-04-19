package com.example.grover.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlantLogItem {
    private String type;
    private String color;
    private Date date;
    private String note = null;
    private SimpleDateFormat sdf;

    public PlantLogItem(String type) {
        this.type = type;
        date = new Date();
        sdf= new SimpleDateFormat("dd/M/yyyy");
        switch(type){
            case "Water": color = "#afe3ff"; break;
            case "Fertilizer": color = "#58dc71"; break;
            case "Repotting": color = "#b98658"; break;
        }
    }

    public PlantLogItem(String type, Date date, String note) {
        this.type = type;
        this.date = date;
        this.note = note;
        switch(type){
            case "Water": color = "#afe3ff"; break;
            case "Fertilizer": color = "#58dc71"; break;
            case "Repotting": color = "#b98658"; break;
        }
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
