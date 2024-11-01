package org.andaevalexander.map;

import org.andaevalexander.species.Animal;
import org.andaevalexander.species.Plant;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class Cell implements Serializable {
    private final int plantInCellCapacity;
    private final List<Plant> plants;
    private Animal currentAnimal;
    private final Random rand = new Random();
    private int waterCapacity;

    public Cell(List<Plant> plants, int plantInCellCapacity, int waterCapacity) {
        this.plants = plants;
        this.plantInCellCapacity = plantInCellCapacity;
        this.waterCapacity = waterCapacity;
    }

    public void addWater(int amount){
        waterCapacity += amount;
    }

    public int reduceWater(int amount){
        waterCapacity -= amount;
        return amount;
    }


    public List<Plant> getPlants() {
        return plants;
    }


    public int getWaterCapacity() {
        return waterCapacity;
    }

    public void addPlant(Plant plant) {
        if (plants.size() < plantInCellCapacity) {
            plants.add(plant);
        }
    }

    public void removePlant(Plant plant) {
        plants.remove(plant);
    }

    public int eatPlant() {
        Plant selectedPlant = plants.get(rand.nextInt(plants.size()));
        plants.remove(selectedPlant);
        return selectedPlant.getCostOfEating();
    }

    public boolean hasPlant(){
        return !plants.isEmpty();
    }

    public int getPlantInCellCapacity() {
        return plantInCellCapacity;
    }

    public void addAnimal(Animal animal) {
        currentAnimal = animal;
    }

    public boolean hasAnimal(){
        return currentAnimal != null;
    }

    public Animal getCurrentAnimal() {
        return currentAnimal;
    }


    public void removeAnimal(){
        currentAnimal = null;
    }


}
