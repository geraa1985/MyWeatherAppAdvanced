package com.example.myweatherappadvanced.calculate;

public class Calculator {
    public static long cToF(long valC) {
        return (int) Math.round(valC * 1.8 + 32);
    }

    public static long gPaToMm(long valGPa) {
        return (int) Math.round(valGPa * 0.75);
    }

    public static long msToKmh(float valMs) {
        return (int) Math.round(valMs * 3.6);
    }
}
