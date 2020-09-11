package com.example.myweatherappadvanced.settings;

import androidx.fragment.app.Fragment;

public class Settings {

    private Fragment currentFragment;

    private static Settings instance;

    private Settings() {}

    public static Settings getInstance() {
        if (instance == null){
            instance = new Settings();
        }
        return instance;
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

}
