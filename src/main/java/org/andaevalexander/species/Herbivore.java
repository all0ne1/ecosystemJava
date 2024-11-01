package org.andaevalexander.species;

import org.andaevalexander.map.Cell;
import org.andaevalexander.map.EcosystemMap;

import static org.andaevalexander.map.EcosystemMap.*;

public class Herbivore extends Species implements Animal{
    private final int herbivoresLifeExpec;
    public Herbivore(String name, int age, int herbivoresLifeExpec, int x, int y) {
        super(name, age);
        this.x = x;
        this.y = y;
        this.herbivoresLifeExpec = herbivoresLifeExpec;
    }

    @Override
    public void eat() {
        Cell currentCell = map[y][x];
        if (currentCell.hasPlant()){
            saturation += currentCell.eatPlant();
        }
    }


    @Override
    public void reproduce() {
        if (saturation > 70){
            for (int[] dir : EcosystemMap.MOVING_DIRECTIONS) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                if (isWithinBounds(newX, newY) && !isAnimalInMovingCell(newX, newY)) {
                    map[newY][newX].addAnimal(new Herbivore(this.name,0, herbivoresLifeExpec, newX, newY));
                    saturation -= (int) (50 * energyConsumptionRate);
                    break;
                }
            }
        }
    }

    @Override
    public void move() {
        moveAnimal(x,y);
    }

    @Override
    public void aging() {
        if (age > herbivoresLifeExpec){
            map[y][x].removeAnimal();
        }
    }



    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }
}
