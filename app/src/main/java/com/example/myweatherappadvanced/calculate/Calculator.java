package com.example.myweatherappadvanced.calculate;

public class Calculator {
    public static int cToF(int valC) {
        return (int) Math.round(valC * 1.8 + 32);
    }

    public static int gPaToMm(int valGPa) {
        return (int) Math.round(valGPa * 0.75);
    }

    public static int msToKmh(int valMs) {
        return (int) Math.round(valMs * 3.6);
    }
}
