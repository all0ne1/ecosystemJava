package org.andaevalexander;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Ecosystem {
    private EcosystemMap ecosystemMap;
    private Cell[][] celledMap;
    private Random rand = new Random();
    private HashMap<List<Integer>, Animal> coordsToCellMap;

    public Ecosystem(EcosystemMap ecosystemMap) {
        this.ecosystemMap = ecosystemMap;
        this.celledMap = ecosystemMap.getMap();
        this.coordsToCellMap = new HashMap<>();
    }

    public void simulate(){
        System.out.println(ecosystemMap.toString());
        for (int y = 0; y < celledMap.length; y++){
            for (int x = 0; x < celledMap[y].length; x++){
                List<Integer> coords = new ArrayList<>();
                coords.add(y);
                coords.add(x);
                if (celledMap[y][x].getCurrentAnimal() != null){
                    coordsToCellMap.put(coords, celledMap[y][x].getCurrentAnimal());
                }
            }
        }
        List<List<Integer>> keysToRemove = new ArrayList<>();
        coordsToCellMap.forEach((coord, animal) -> {
            if (animal instanceof Herbivore) {
                animal.eat();
                animal.move();
                keysToRemove.add(coord);
            }
        });
        keysToRemove.forEach(coordsToCellMap::remove);
        keysToRemove.clear();
        coordsToCellMap.forEach((coord, animal) -> {
            animal.eat();
            animal.move();
        });
        coordsToCellMap.clear();
    }


    private void loadAnimals(String path){
        try (BufferedReader bf = new BufferedReader(new FileReader(path))) {
            for (String line = bf.readLine(); line != null; line = bf.readLine()) {
                String[] data = line.split(",");
            }
        } catch (IOException e){
            System.out.println("Error loading animals: " + e.getMessage());
        }

    }
}
