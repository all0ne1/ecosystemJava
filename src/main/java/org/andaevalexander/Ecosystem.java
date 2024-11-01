package org.andaevalexander;

import org.andaevalexander.climate.Climate;
import org.andaevalexander.species.*;
import org.andaevalexander.map.Cell;
import org.andaevalexander.map.EcosystemMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Ecosystem {
    private final EcosystemMap ecosystemMap;
    private final Cell[][] celledMap;
    private final HashMap<List<Integer>, Cell> coordsToCellMap;
    private Random rand = new Random();
    private final Climate climate;

    public Ecosystem(EcosystemMap ecosystemMap, int humidity, double temperature) {
        this.ecosystemMap = ecosystemMap;
        this.celledMap = EcosystemMap.getMap();
        this.coordsToCellMap = new HashMap<>();
        this.climate = Climate.getInstance(temperature, humidity);
    }


    public void simulate(){
        System.out.println(ecosystemMap);
        // вода распространяется
        System.out.println("Вода распространяется\n");
        for (int y = 0; y < celledMap.length; y++) {
            for (int x = 0; x < celledMap[y].length; x++) {
                EcosystemMap.spreadWater(x,y);
            }
        }

        //Растения растут
        for (int y = 0; y < celledMap.length; y++) {
            for (int x = 0; x < celledMap[y].length; x++) {
                Cell currentCell = celledMap[y][x];
                if (!currentCell.getPlants().isEmpty()){
                    List<Plant> plantsCopy = new ArrayList<>(currentCell.getPlants());

                    for (Plant plant : plantsCopy) {
                        plant.consumeResources();
                        plant.grow();
                    }
                }
            }
        }
        System.out.println("Растения растут");
        System.out.println(ecosystemMap);

        for (int y = 0; y < celledMap.length; y++){
            for (int x = 0; x < celledMap[y].length; x++){
                List<Integer> coords = new ArrayList<>(2);
                coords.add(y);
                coords.add(x);
                if (celledMap[y][x].getCurrentAnimal() != null){
                    coordsToCellMap.put(coords, celledMap[y][x]);
                }
            }
        }
        // Травоядные ходят
        coordsToCellMap.forEach((coord, cell) -> {
            Animal animal = cell.getCurrentAnimal();
            if (animal instanceof Herbivore) {
                animal.eat();
                animal.move();
                animal.reproduce();
            }
        });
        System.out.println("Ход травоядных");
        System.out.println(ecosystemMap);
        // Хищники ходят
        coordsToCellMap.forEach((coord, cell) -> {
            Animal animal = cell.getCurrentAnimal();
            if (animal instanceof Predator) {
                animal.eat();
                animal.move();
                animal.reproduce();
            }
        });
        System.out.println("Ход хищников");
        System.out.println(ecosystemMap);
        // Пополнение источников воды
        System.out.println("Пополнение источников воды");
        EcosystemMap.getWaterSourceMap().forEach((coord, waterSouce) -> {
            waterSouce.addWater(rand.nextInt(100));
        });
        // Старение
        System.out.println("Происходит старение...");
        for (int y = 0; y < celledMap.length; y++) {
            for (int x = 0; x < celledMap[y].length; x++) {
                Cell currentCell = celledMap[y][x];

                // Старение всех растений в клетке
                if (!currentCell.getPlants().isEmpty()) {
                    List<Plant> plantsCopy = new ArrayList<>(currentCell.getPlants());
                    for (Plant plant : plantsCopy) {
                        plant.aging();
                    }
                }

                // Старение животного в клетке, если оно там есть
                Animal animal = currentCell.getCurrentAnimal();
                if (animal != null) {
                    animal.aging();
                }
            }
        }
        coordsToCellMap.clear();
        EcosystemMap.saveState("save.txt");
    }
}
