package com.aldo.aldendino.thermoswitch;

public class Converter {
    public static double convertTempurature(double input, boolean isCtoF) {
        double converted;
        if(isCtoF) converted = input * 9 / 5 + 32;
        else converted = (input - 32) * 5 / 9;
        return converted;
    }

    public static double convertDistance(double input, boolean isKtoM) {
        double converted;
        if(isKtoM) converted = input * 0.621371;
        else converted = input * 1.60934;
        return converted;
    }

    public static double convertWeight(double input, boolean isKtoP) {
        double converted;
        if(isKtoP) converted = input * 2.20462;
        else converted = input * 0.453592;
        return converted;
    }

    public static double convertVolumeUS(double input, boolean isLtoG) {
        double converted;
        if(isLtoG) converted = input * 0.264172; // US gallon
        else converted = input * 3.78541; // US gallon
        return converted;
    }

    public static double convertVolumeImperial(double input, boolean isLtoG) {
        double converted;
        if(isLtoG) converted = input * 0.219969; // Imperial gallon
        else converted = input * 4.54609; // Imperial gallon
        return converted;
    }

    public enum ConversionType {
        TEMPERATURE, DISTANCE, WEIGHT, VOLUME_US, VOLUME_IMPERIAL;
    }
}
