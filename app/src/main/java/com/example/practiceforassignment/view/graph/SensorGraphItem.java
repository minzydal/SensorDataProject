package com.example.practiceforassignment.view.graph;

import java.util.ArrayList;

public class SensorGraphItem {

    String value;
    String nameOfValue, maxValue, minValue, currentValue;
    ArrayList<Integer> graphPointList = new ArrayList<Integer>();

    public SensorGraphItem(String value, String maxValue, String minValue, String currentValue, String nameOfValue) {
        this.value = value;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.currentValue = currentValue;
        this.nameOfValue = nameOfValue;
    }

    public void addGraphPointList(int value) {
        graphPointList.add(value);
    }

    public void setGraphPointList() {
        graphPointList.clear();
    }

    public String getNameOfValue() {
        return nameOfValue;
    }

}
