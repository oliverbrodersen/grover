package com.example.grover.models;

import java.util.Comparator;

public class OrderLogByDate implements Comparator<PlantLogItem> {

    @Override
    public int compare(PlantLogItem o1, PlantLogItem o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}
