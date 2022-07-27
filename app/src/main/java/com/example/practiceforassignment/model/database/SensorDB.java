//실제 로컬 DB인 Room을 생성
package com.example.practiceforassignment.model.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.practiceforassignment.model.Converter;

@Database(entities = {SensorDataList.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class SensorDB extends RoomDatabase {
    public abstract SensorDataListDao dao();
}
