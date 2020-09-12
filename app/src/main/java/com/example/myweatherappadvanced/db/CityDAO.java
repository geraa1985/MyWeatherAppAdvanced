package com.example.myweatherappadvanced.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CityDAO {

    @Query("SELECT * FROM citydb")
    List<CityDB> getAll();

    @Query("SELECT * FROM citydb ORDER BY id ASC")
    List<CityDB> sortByDate();

    @Query("SELECT * FROM citydb ORDER BY name DESC")
    List<CityDB> sortByCity();

    @Query("SELECT * FROM citydb ORDER BY temperature DESC")
    List<CityDB> sortByTemp();

    @Insert
    void insert(CityDB cityDB);

    @Query("DELETE FROM citydb")
    void clearAll();

    @Query("DELETE FROM citydb WHERE id = :id")
    void deleteByID(long id);
}
