package com.example.myweatherappadvanced.settings;

public class Settings {

    private boolean isF;
    private boolean isMM;
    private boolean isKMH;
//    private boolean isNight;

    private static Settings instance;

    private Settings() {}

    public static Settings getInstance() {
        if (instance == null){
            instance = new Settings();
        }
        return instance;
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

//    public boolean isNight() {
//        return isNight;
//    }
//
//    public void setNight(boolean night) {
//        isNight = night;
//    }


}
