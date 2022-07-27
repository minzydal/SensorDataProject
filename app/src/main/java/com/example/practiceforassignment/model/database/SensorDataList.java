// Entity
package com.example.practiceforassignment.model.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.practiceforassignment.model.Converter;
import com.example.practiceforassignment.view.graph.SensorGraphItem;
import com.example.practiceforassignment.view.list.SensorListItem;
import java.util.ArrayList;

@Entity

public class SensorDataList {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @TypeConverters(Converter.class)
    private ArrayList<SensorListItem> dataList;
    @TypeConverters(Converter.class)
    private ArrayList<SensorGraphItem> graphList;

    public SensorDataList() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<SensorListItem> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<SensorListItem> dataList) {
        this.dataList = dataList;
    }

    public ArrayList<SensorGraphItem> getGraphList() {
        return graphList;
    }

    public void setGraphList(ArrayList<SensorGraphItem> graphList) {
        this.graphList = graphList;
    }
}