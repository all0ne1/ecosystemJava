package org.andaevalexander;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Predator extends Species implements Animal{
    String gender;
    int mapWidth;
    int mapHeight;
    Random rand = new Random();
    public Predator(String name, int age, double growthRate, String gender, EcosystemMap ecosystemMap, int x, int y) {
        super(name, age, growthRate, ecosystemMap);
        this.x = x;
        this.y = y;
        this.gender = gender;
        this.mapWidth = ecosystemMap.getWidth();
        this.mapHeight = ecosystemMap.getHeight();
    }

    @Override
    public void eat() {
        List<Cell> adjacentCells = getAdjacentCells(x,y);
        for (Cell cell : adjacentCells){
            int chanceOfSuccess = rand.nextInt(100);
            if (cell.getCurrentAnimal() instanceof Herbivore){
                if (chanceOfSuccess > 60){
                    Herbivore huntedAnimal = (Herbivore) cell.getCurrentAnimal();
                    map[y][x].removeAnimal();
                    saturation += (int) (huntedAnimal.getSaturation() * 0.3);
                    cell.addAnimal(this);
                    break;
                }
            }
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

    private List<Cell> getAdjacentCells(int x, int y){
        List<Cell> adjacentCells = new ArrayList<>();
        // Вниз
        if (isWithinBounds(x, y + 1)) adjacentCells.add(map[y+1][x]);
        // Влево
        if (isWithinBounds(x - 1, y)) adjacentCells.add(map[y][x-1]);
        // Вправо
        if (isWithinBounds(x + 1, y)) adjacentCells.add(map[y][x+1]);
        // Вверх
        if (isWithinBounds(x, y - 1)) adjacentCells.add(map[y-1][x]);

        return adjacentCells;
    }


}
