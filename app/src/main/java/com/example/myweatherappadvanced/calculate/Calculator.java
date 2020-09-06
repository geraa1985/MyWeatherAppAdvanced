package com.example.myweatherappadvanced.calculate;

public class Calculator {
    public static long cToF(Double valC) {
        return (int) Math.round(valC * 1.8 + 32);
    }

    public static long gPaToMm(long valGPa) {
        return (int) Math.round(valGPa * 0.75);
    }

    public static long msToKmh(Double valMs) {
        return (int) Math.round(valMs * 3.6);
    }
}
