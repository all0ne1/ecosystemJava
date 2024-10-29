package org.andaevalexander;

import java.util.Random;

public class Herbivore extends Species implements Animal{
    private final String gender;
    private final int mapWidth;
    private final int mapHeight;

    public Herbivore(String name, int age, double growthRate, String gender, EcosystemMap ecosystemMap, int x, int y) {
        super(name, age, growthRate, ecosystemMap);
        this.x = x;
        this.y = y;
        this.gender = gender;
        this.mapWidth = map[0].length;
        this.mapHeight = map.length;
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

    }

    @Override
    public void move() {
        int[][] directions = {
                {0, 1},   // вниз
                {-1, 0},  // влево
                {1, 0},   // вправо
                {0, -1}   // вверх
        };

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            if (isWithinBounds(newX, newY) && !isAnimalInMovingCell(newX, newY)) {
                map[newY][newX].addAnimal(this);
                map[y][x].removeAnimal();
                x = newX;
                y = newY;
                break;
            }
        }
    }

    private boolean isAnimalInMovingCell(int x, int y) {
        return map[y][x].getCurrentAnimal() != null;
    }

    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < mapWidth && y >= 0 && y < mapHeight;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }
}
