package org.andaevalexander.species;

import org.andaevalexander.map.Cell;
import org.andaevalexander.map.EcosystemMap;

import java.io.Serializable;
import java.util.Random;

import static org.andaevalexander.Config.*;

public class Flora extends Species implements Plant, Serializable {
    private int waterAmount = minPlantsWaterAmount;
    private final Random rand = new Random();
    private final int plantsLifeExpec;
    private int energyToGrowth;
    public Flora(String name, int age, int plantsLifeExpec, int x, int y) {
        super(name, age);
        this.x = x;
        this.y = y;
        this.plantsLifeExpec = plantsLifeExpec;
        this.energyToGrowth = (int) (saturationNeedToGrowthPlants * energyConsumptionRate);
    }

    @Override
    public void grow() {
        if (waterAmount > energyToGrowth && saturation > energyToGrowth) {
            if (map[y][x].getPlants().size() < map[y][x].getPlantInCellCapacity()){
                map[y][x].addPlant(new Flora(this.name, 0, plantsLifeExpec, x, y));
            } else{
                for (int [] dir : EcosystemMap.MOVING_DIRECTIONS){
                    int nextX = x + dir[0];
                    int nextY = y + dir[1];
                    if (EcosystemMap.isWithinBounds(nextX, nextY)){
                        map[nextY][nextX].addPlant(this);
                        waterAmount -= energyToGrowth;
                        saturation -= energyToGrowth;
                        break;
                    }
                }
            }

        }
    }

    @Override
    public void consumeResources() {
        Cell currentCell = map[y][x];
        waterAmount += currentCell.reduceWater(rand.nextInt(10,40));
        saturation += plantsSaturationPerTurns;
    }

    @Override
    public int getCostOfEating() {
        return (waterAmount + saturation) / 4;
    }

    @Override
    public void aging() {
        if (age > plantsLifeExpec){
            map[y][x].removePlant(this);
        }
    }

}
