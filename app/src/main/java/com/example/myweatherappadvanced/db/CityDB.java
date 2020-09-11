package com.example.myweatherappadvanced.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.myweatherappadvanced.calculate.CurrentTime;

@Entity
public class CityDB {

    @PrimaryKey (autoGenerate = true)
    public long id;
    public String date;
    public String name;
    public String temperature;

    public CityDB () {
        this.date = CurrentTime.getCurrentTime();
    }
}
