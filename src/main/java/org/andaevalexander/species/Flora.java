package org.andaevalexander.species;

import org.andaevalexander.map.Cell;
import org.andaevalexander.map.EcosystemMap;

import java.util.Random;

public class Flora extends Species implements Plant {
    private int energy = 10;
    private int waterAmount = 10;
    private Random rand = new Random();
    private final int plantsLifeExpec;
    public Flora(String name, int age, int plantsLifeExpec, int x, int y) {
        super(name, age);
        this.x = x;
        this.y = y;
        this.plantsLifeExpec = plantsLifeExpec;
    }

    @Override
    public void grow() {
        if (waterAmount > 100 && energy > 100) {
            if (map[y][x].getPlants().size() < map[y][x].getPlantInCellCapacity()){
                map[y][x].addPlant(new Flora(this.name, 0, plantsLifeExpec, x, y));
            } else{
                for (int [] dir : EcosystemMap.MOVING_DIRECTIONS){
                    int nextX = x + dir[0];
                    int nextY = y + dir[1];
                    if (EcosystemMap.isWithinBounds(nextX, nextY)){
                        map[nextY][nextX].addPlant(this);
                        waterAmount -= 100;
                        energy -= 100;
                        break;
                    }
                }
            }

        }
    }

    @Override
    public void consumeResources() {
        Cell currentCell = map[y][x];
        waterAmount += currentCell.reduceWater(rand.nextInt(10,50));
        energy += 20;
    }

    @Override
    public int getCostOfEating() {
        return (waterAmount + energy) / 2;
    }

    @Override
    public void aging() {
        if (age > plantsLifeExpec){
            map[y][x].removePlant(this);
        }
    }

}
