package com.example.grover.models;

import java.util.Comparator;

public class CompareWaterBy implements Comparator<Plant>
{
    @Override
    public int compare(Plant o1, Plant o2) {
        return o1.waterToday() - o2.waterToday();
    }
}
