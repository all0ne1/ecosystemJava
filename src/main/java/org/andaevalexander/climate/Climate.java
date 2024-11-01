package org.andaevalexander.climate;

import java.io.Serializable;

public class Climate implements Serializable {
    private static double temperature;
    private static int humidity;
    private static Climate instance;

    private Climate(double temperature, int humidity) {
        Climate.temperature = temperature;
        Climate.humidity = humidity;
    }

    public static Climate getInstance(double temperature, int humidity) {
        if (instance == null) {
            return new Climate(temperature, humidity);
        }
        return instance;
    }

    public static Climate getInstance() {
        return instance;
    }

    public static double getTemperature() {
        return temperature;
    }

    public static int getHumidity() {
        return humidity;
    }

    public static Conditions getConditions() {
        if (temperature <= 0) {
            return humidity >= 80 ? Conditions.BAD : Conditions.MODERATE;
        } else if (temperature < 15) {
            if (humidity > 70){
                return Conditions.BAD;
            }
            return humidity < 30 ? Conditions.GOOD : Conditions.MODERATE;
        } else if (temperature <= 30) {
            if (humidity > 80){
                return Conditions.MODERATE;
            }
            return humidity < 30 ? Conditions.GOOD : Conditions.MODERATE;
        } else {
            if (humidity > 80){
                return Conditions.BAD;
            }
            return humidity < 30 ? Conditions.MODERATE : Conditions.BAD;
        }
    }
}
