package com.example.myweatherappadvanced.settings;

import androidx.fragment.app.Fragment;

import java.util.LinkedList;

public class Settings {

    private LinkedList<String> citiesList;

    private boolean isF;
    private boolean isMM;
    private boolean isKMH;
    private boolean isNight;
    private Fragment currentFragment;
    private boolean isCityFromList;

    private static Settings instance;

    private Settings() {}

    public static Settings getInstance() {
        if (instance == null){
            instance = new Settings();
        }
        return instance;
    }

    public LinkedList<String> getCitiesList() {
        return citiesList;
    }

    public void setCitiesList(LinkedList<String> citiesList) {
        this.citiesList = citiesList;
    }

    public boolean isF() {
        return isF;
    }

    public void setF(boolean f) {
        isF = f;
    }

    public boolean isMM() {
        return isMM;
    }

    public void setMM(boolean MM) {
        isMM = MM;
    }

    public boolean isKMH() {
        return isKMH;
    }

    public void setKMH(boolean KMH) {
        isKMH = KMH;
    }

    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    public boolean isCityFromList() {
        return isCityFromList;
    }

    public void setCityFromList(boolean cityFromList) {
        isCityFromList = cityFromList;
    }
}
