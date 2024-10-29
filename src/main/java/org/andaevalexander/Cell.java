package org.andaevalexander;

import java.util.List;
import java.util.Random;

public class Cell {
    private final int plantInCellCapacity;
    private final List<Plant> plants;
    private Animal currentAnimal;
    Random rand = new Random();
    public Cell(List<Plant> plants, int plantInCellCapacity) {
        this.plants = plants;
        this.plantInCellCapacity = plantInCellCapacity;
    }

    public boolean addPlant(Plant plant) {
        if (plants.size() < plantInCellCapacity) {
            plants.add(plant);
            return true;
        } else{
            return false;
        }
    }

    public int eatPlant() {
        Plant selectedPlant = plants.get(rand.nextInt(plants.size()));
        plants.remove(selectedPlant);
        return selectedPlant.getCostOfEating();
    }

    public boolean hasPlant(){
        return !plants.isEmpty();
    }

    public void addAnimal(Animal animal) {
        currentAnimal = animal;
    }

    public void removeAnimal(){
        currentAnimal = null;
    }

    public boolean hasAnimal(){
        return currentAnimal != null;
    }

    public Animal getCurrentAnimal() {
        return currentAnimal;
    }
}
