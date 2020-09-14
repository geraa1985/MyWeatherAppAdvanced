package com.example.myweatherappadvanced;

import android.app.Application;

import androidx.room.Room;

import com.example.myweatherappadvanced.db.AppDatabase;

public class App extends Application {

    private static App instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database.db")
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
