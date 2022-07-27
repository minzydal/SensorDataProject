//Room DB에 접근하기 위한 Dao를 생성
package com.example.practiceforassignment.model.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface SensorDataListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SensorDataList sensorDataList);

    @Query("DELETE FROM SensorDataList")
    void deleteAll();

    @Query("SELECT * FROM SensorDataList")
    List<SensorDataList> getAll();

}


