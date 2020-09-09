package com.example.myweatherappadvanced.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CityDB.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CityDAO cityDAO();
}
