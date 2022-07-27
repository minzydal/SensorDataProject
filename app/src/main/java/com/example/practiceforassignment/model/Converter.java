// 단일 DB 열에 값을 저장하려고 하는 맞춤 데이터 유형 지정자
package com.example.practiceforassignment.model;

import androidx.room.TypeConverter;

import com.example.practiceforassignment.view.graph.SensorGraphItem;
import com.example.practiceforassignment.view.list.SensorListItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converter {
    @TypeConverter
    public static ArrayList<SensorListItem> fromString(String value) {
        Type listType = new TypeToken<ArrayList<SensorListItem>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<SensorListItem> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static ArrayList<SensorGraphItem> fromString2(String value) {
        Type listType = new TypeToken<ArrayList<SensorGraphItem>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList2(ArrayList<SensorGraphItem> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


}

