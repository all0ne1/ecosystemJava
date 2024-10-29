package org.andaevalexander;


import java.util.ArrayList;

public class EcosystemMap {
    private static EcosystemMap instance;
    private final Cell[][] map;
    private final int width;
    private final int height;

    public EcosystemMap(int height, int width) {
        this.height = height;
        this.width = width;
        map = new Cell[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[y][x] = new Cell(new ArrayList<>(), 5);
            }
        }

        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                map[y][x] = new Cell(new ArrayList<>(), 5);
                Predator predator = new Predator("Bear", 4,1.0,"male",this, x, y);
                map[y][x].addAnimal(predator);
            }
        }
        for (int y = height - 2; y < height; y++) {
            for (int x = width - 2; x < width; x++) {
                map[y][x] = new Cell(new ArrayList<>(), 5);
                Herbivore herbivore = new Herbivore("Deer", 4,1.0,"male",this, x, y);
                map[y][x].addAnimal(herbivore);
            }
        }

    }

    public static EcosystemMap getInstance(int height, int width) {
        if (instance == null) {
            instance = new EcosystemMap(height, width);
        }
        return instance;
    }

    public Cell[][] getMap() {
        return map;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Cell[] row : map) {
             for (Cell cell : row) {
                if (cell != null){
                    if (cell.getCurrentAnimal() instanceof Herbivore) res.append(" [ H ] ");
                    else if (cell.getCurrentAnimal() instanceof Predator) res.append(" [ P ] ");
                    else if (cell.hasAnimal() && cell.hasPlant()) res.append(" [A/T] ");
                    else if (!cell.hasAnimal() && cell.hasPlant()) res.append(" [ T ] ");
                    else res.append(" [  ] ");
                } else{
                    res.append(" [  ] ");
                }

            }
            res.append("\n");
        }
        return res.toString();
    }
}
