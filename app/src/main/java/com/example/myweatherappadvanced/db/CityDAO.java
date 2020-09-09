package com.example.myweatherappadvanced.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CityDAO {

    @Query("SELECT * FROM citydb")
    List<CityDB> getAll();

    @Insert
    void insert(CityDB cityDB);

    @Update
    void update(CityDB cityDB);

    @Delete
    void delete(CityDB cityDB);

}
