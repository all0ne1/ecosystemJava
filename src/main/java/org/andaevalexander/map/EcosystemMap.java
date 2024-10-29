package org.andaevalexander.map;

import org.andaevalexander.species.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.andaevalexander.Colors.*;

public class EcosystemMap {
    private static EcosystemMap instance;
    private static Cell[][] map;
    private static int width;
    private static int height;
    private Random rand = new Random();

    public final static HashMap<List<Integer>, Cell> waterSouceMap = new HashMap<>();
    public final static int[][] MOVING_DIRECTIONS =  {
            {0, 1},   // вниз
            {-1, 0},  // влево
            {1, 0},   // вправо
            {0, -1}   // вверх
    };

    private EcosystemMap(int height, int width) {
        EcosystemMap.height = height;
        EcosystemMap.width = width;
        map = new Cell[height][width];
        // init cells
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[y][x] = new Cell(new ArrayList<>(), 5, rand.nextInt(0,150));
            }
        }

        addRandomWaterSources(5);

        //add plants
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                for (int i = 0; i < rand.nextInt(5); i++) {
                    map[y][x].addPlant(new Flora("Grass", 2,8, x,y));
                }
            }
        }



        // Добавляем хищников в верхний левый угол
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                Predator predator = new Predator("Bear", 4,5, x, y);
                map[y][x].addAnimal(predator);
            }
        }

        // Добавляем травоядных в нижний правый угол
        for (int y = height - 2; y < height; y++) {
            for (int x = width - 2; x < width; x++) {
                Herbivore herbivore = new Herbivore("Deer", 4, 6, x, y);
                map[y][x].addAnimal(herbivore);
            }
        }
    }

    public static void spreadWater(int start_x, int start_y){
        for (int[] dir : MOVING_DIRECTIONS){
            int newX = start_x + dir[0];
            int newY = start_y + dir[1];
            Cell currentCell = map[start_y][start_x];
            if (isWithinBounds(newX, newY)){
                Cell movingCell = map[newY][newX];
                movingCell.addWater(currentCell.getWaterCapacity() / 8);
                currentCell.reduceWater(currentCell.getWaterCapacity() / 8);
            }
        }
    }


    public static EcosystemMap getInstance(int height, int width) {
        if (instance == null) {
            instance = new EcosystemMap(height, width);
        }
        return instance;
    }

    public static EcosystemMap getInstance() {
        return instance;
    }

    public static Cell[][] getMap() {
        return map;
    }

    public static boolean isAnimalInMovingCell(int x, int y) {
        return map[y][x].getCurrentAnimal() != null;
    }

    public static boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public static void moveAnimal(int x, int y) {
        Species curAnimal = (Species) map[y][x].getCurrentAnimal();
        for (int[] dir : EcosystemMap.MOVING_DIRECTIONS) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            if (isWithinBounds(newX, newY) && !isAnimalInMovingCell(newX, newY)) {
                map[newY][newX].addAnimal((Animal) curAnimal);
                curAnimal.setX(newX);
                curAnimal.setY(newY);
                map[y][x].removeAnimal();
                break;
            }
        }
    }

    private void addRandomWaterSources(int numSources) {
        for (int i = 0; i < numSources; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            List<Integer> coords = new ArrayList<>(2){{
                add(x);
                add(y);
            }};
            waterSouceMap.put(coords, map[y][x]);
            map[y][x].addWater(rand.nextInt(151,250)); // Set initial water level at source to capacity
        }
    }

    public static HashMap<List<Integer>, Cell> getWaterSourceMap() {
        return waterSouceMap;
    }

    @Override
    public String toString() {


        StringBuilder res = new StringBuilder();


        // Верхняя граница
        res.append("+" + "-".repeat(width * 5) + "+\n");

        for (int y = 0; y < height; y++) {
            res.append("|");
            for (int x = 0; x < width; x++) {
                Cell cell = map[y][x];
                if (cell != null) {
                    int currentPlantsInCell = cell.getPlants().size();
                    if (cell.hasAnimal() && cell.hasPlant()) {
                        if (cell.getCurrentAnimal() instanceof Predator){
                            res.append(RED + String.format(" [P,%dT]",currentPlantsInCell) + RESET);
                        } else{
                            res.append(GREEN + String.format(" [H,%dT]",currentPlantsInCell) + RESET);
                        }
                    } else if (cell.getCurrentAnimal() instanceof Herbivore) {
                        res.append(GREEN + " [H]" + RESET); // Травоядное
                    } else if (cell.getCurrentAnimal() instanceof Predator) {
                        res.append(RED + " [P]" + RESET); // Хищник
                    } else if (cell.hasPlant() && !cell.hasAnimal()) {
                        res.append(YELLOW + String.format(" [%dT]", currentPlantsInCell) + RESET); // Растение
                    } else {
                        res.append(" [  ] ");
                    }
                } else {
                    res.append(" [  ] ");
                }
            }
            res.append("|\n");  // Правая граница
        }

        // Нижняя граница
        res.append("+" + "-".repeat(width * 5) + "+\n");

        return res.toString();
    }


}
