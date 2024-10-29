package org.andaevalexander.species;

import org.andaevalexander.map.Cell;
import org.andaevalexander.map.EcosystemMap;

import java.util.Random;

import static org.andaevalexander.map.EcosystemMap.*;

public class Predator extends Species implements Animal{
    Random rand = new Random();
    private final int predatorsLifeExpec;
    public Predator(String name, int age, int predatorsLifeExpec, int x, int y) {
        super(name, age);
        this.predatorsLifeExpec = predatorsLifeExpec;
        this.x = x;
        this.y = y;
    }

    @Override
    public void eat() {
        for (int[] dir : EcosystemMap.MOVING_DIRECTIONS){

            int newX = x + dir[0];
            int newY = y + dir[1];
            Cell currentCell = map[y][x];
            if (isWithinBounds(newX, newY)) {
                int chanceOfSuccess = rand.nextInt(1,100);
                Cell huntingCell = map[newY][newX];
                if (huntingCell.getCurrentAnimal() instanceof Herbivore && chanceOfSuccess > 40) {
                    Herbivore huntedAnimal = (Herbivore) huntingCell.getCurrentAnimal();
                    map[newY][newX].removeAnimal();
                    saturation += (int) (huntedAnimal.getSaturation() * 0.3);
                    map[newY][newX].addAnimal(this);
                    Predator predator = (Predator) huntingCell.getCurrentAnimal();
                    currentCell.removeAnimal();
                    System.out.println("Predator in cell with coords (x,y): " + (y+1) + ", " + (x+1) + " succeed with hunting!");
                    System.out.println(EcosystemMap.getInstance());
                    predator.setX(newX);
                    predator.setY(newY);
                    break;
                }
            }
        }
    }


    @Override
    public void reproduce() {
        if (saturation > 100){
            for (int[] dir : EcosystemMap.MOVING_DIRECTIONS) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                if (isWithinBounds(newX, newY) && !isAnimalInMovingCell(newX, newY)) {
                    map[newY][newX].addAnimal(new Predator(this.name,0, predatorsLifeExpec, newX, newY));
                    saturation -= 50;
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
        age++;
        if (age > predatorsLifeExpec){
            map[y][x].removeAnimal();
        }
    }


}
