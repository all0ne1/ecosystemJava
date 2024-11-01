package org.andaevalexander.climate;

import org.andaevalexander.Ecosystem;
import org.andaevalexander.map.Cell;
import org.andaevalexander.map.EcosystemMap;

import java.io.Serializable;

import static org.andaevalexander.Config.maxPlantInCell;
import static org.andaevalexander.Config.maxWaterInCell;

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
            instance = new Climate(temperature, humidity);
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

    public static void updateClimate() {
        Cell[][] map = EcosystemMap.getMap();
        int totalWater = 0;
        int totalPlants = 0;
        int totalAnimals = 0;
        int totalCells = map.length * map[0].length;

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                totalWater += map[y][x].getWaterCapacity();
                totalPlants += map[y][x].getPlants().size();
                totalAnimals += map[y][x].hasAnimal() ? 1 : 0;
            }
        }

        double maxTemp = 70;
        double minTemp = -50;

        double waterEffect = (double) totalWater / (maxWaterInCell * totalCells);
        double plantEffect = (double) totalPlants / (maxPlantInCell * totalCells);
        double animalEffect = (double) totalAnimals / totalCells;

        double targetTemperature = minTemp + (maxTemp - minTemp) * (0.5 * waterEffect + 0.2 * plantEffect + 0.1 * animalEffect);


        double tempChangeRate = 1.5;
        if (temperature < targetTemperature) {
            temperature = Math.min(maxTemp, temperature + tempChangeRate);
        } else {
            temperature = Math.max(minTemp, temperature - tempChangeRate);
        }

        int targetHumidity = (int) (waterEffect * 100);

        int humidityChangeRate = 2;
        if (humidity < targetHumidity) {
            humidity = Math.min(100, humidity + humidityChangeRate);
        } else {
            humidity = Math.max(0, humidity - humidityChangeRate);
        }
    }



    @Override
    public String toString() {
        return "Climate{ " +
                "температура=" + temperature + "}"
                + ", влажность=" + humidity + "}";
    }
}
