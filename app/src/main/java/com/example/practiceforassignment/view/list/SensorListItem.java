package com.example.practiceforassignment.view.list;

public class SensorListItem {
    int id;
    String sensorName, sensorData, sensorUnit;

    public SensorListItem(int id, String randomName, String randomNum, String randomUnit) {
        this.id = id;
        this.sensorName = randomName;
        this.sensorData = randomNum;
        this.sensorUnit = randomUnit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSensorName() {
        return sensorName;
    }

    public String getSensorData() {
        return sensorData;
    }

    public String getSensorUnit() {
        return sensorUnit;
    }

}
