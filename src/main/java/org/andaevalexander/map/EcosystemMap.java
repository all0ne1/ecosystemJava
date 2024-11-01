package org.andaevalexander.map;

import org.andaevalexander.Config;
import org.andaevalexander.climate.Climate;
import org.andaevalexander.species.*;

import java.io.*;
import java.util.*;

import static org.andaevalexander.Config.*;
import static org.andaevalexander.utils.Colors.*;

public class EcosystemMap implements Serializable {
    private static EcosystemMap instance;
    private static Cell[][] map;
    public static final String UUID_FILE = UUID.randomUUID().toString();
    public static final String SAVE_FILE = "./savedSimulations/simulation_state_" + UUID_FILE + ".txt";
    private static int width;
    private static int height;
    private static final Random rand = new Random();

    public final static HashMap<List<Integer>, Cell> waterSouceMap = new HashMap<>();
    public final static int[][] MOVING_DIRECTIONS =  {
            {0, 1},   // вниз
            {-1, 0},  // влево
            {1, 0},   // вправо
            {0, -1}   // вверх
    };

    private EcosystemMap(int height, int width, Cell[][] loadedMap) {
        EcosystemMap.height = height;
        EcosystemMap.width = width;
        map = loadedMap;
    }

    private EcosystemMap(int height, int width) {
        EcosystemMap.height = height;
        EcosystemMap.width = width;
        map = new Cell[height][width];
        // init cells
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[y][x] = new Cell(new ArrayList<>(), maxPlantInCell, rand.nextInt(0,maxWaterInCell));
            }
        }

        addRandomWaterSources(numOfWaterSources);

        //add plants
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                for (int i = 0; i < rand.nextInt(0, maxPlantInCell); i++) {
                    map[y][x].addPlant(new Flora("Grass", rand.nextInt(rangeOfPlantsAge + 1),plantsLifeExpectancy, x,y));
                }
            }
        }


        for (int i = 0; i < startAmountOfPredators; i++){
            if (startAmountOfPredators > width * height){
                System.out.println("Превышен размер карты для хищников");
                break;
            }
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            Animal predator = new Predator("Bear", rand.nextInt(rangeOfPredatorsAge),predatorsLifeExpectancy, x, y);
            map[y][x].addAnimal(predator);
        }

        for (int i = 0; i < startAmountOfHerbivore; i++) {
            if (startAmountOfHerbivore > width * height - startAmountOfPredators){
                System.out.println("Превышен размер карты для травоядных");
            }
            int x,y;
            do{
                x = rand.nextInt(width);
                y = rand.nextInt(height);
            } while (!map[y][x].hasAnimal());
            Animal herbivore = new Herbivore("Dear", rand.nextInt(rangeOfHerbivoreAge), herbivoreLifeExpectancy, x, y);
            map[y][x].addAnimal(herbivore);

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


    public static EcosystemMap getInstance() {
        return instance;
    }

    public static EcosystemMap getInstance(int height, int width) {
        if (instance == null) {
            instance = new EcosystemMap(height, width);
        }
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
        List<List<Integer>> directions = getAvailablePaths(x,y);
        if (directions.isEmpty()) return;
        List<Integer> movingDir = directions.get(rand.nextInt(directions.size()));
        int nextX = movingDir.get(0);
        int nextY = movingDir.get(1);
        map[nextY][nextX].addAnimal((Animal) curAnimal);
        curAnimal.setX(nextX);
        curAnimal.setY(nextY);
        map[y][x].removeAnimal();
    }

    private static List<List<Integer>> getAvailablePaths(int start_x, int start_y){
        List<List<Integer>> availablePaths = new ArrayList<>(4);
        for (int[] dir : EcosystemMap.MOVING_DIRECTIONS) {
            int newX = start_x + dir[0];
            int newY = start_y + dir[1];
            if (isWithinBounds(newX, newY) && !isAnimalInMovingCell(newX, newY)) {
                availablePaths.add(new ArrayList<>(2) {{
                    add(newX);
                    add(newY);
                }});
            }
        }
        return availablePaths;
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
            map[y][x].addWater(rand.nextInt((int) (maxWaterInCell * 0.6),maxWaterInCell)); // Set initial water level at source to capacity
        }
    }

    public static HashMap<List<Integer>, Cell> getWaterSourceMap() {
        return waterSouceMap;
    }

    public static void saveState(){

        try (FileOutputStream fileOut = new FileOutputStream(SAVE_FILE);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(EcosystemMap.getMap()); // Сохранение массива клеток
            out.writeObject(Climate.getInstance());   // Сохранение климата
            Config.saveState(out);
            System.out.println("Состояние сохранено в " + SAVE_FILE);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении состояния: " + e.getMessage());
        }
    }


    public static EcosystemMap loadState(String filename){
        try (FileInputStream fileIn = new FileInputStream("./savedSimulations/" + filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            Cell[][] loadedCelledMap = (Cell[][]) in.readObject();
            Climate loadedClimate = (Climate) in.readObject();
            instance = new EcosystemMap(loadedCelledMap.length, loadedCelledMap[0].length, loadedCelledMap);
            Climate.getInstance(loadedClimate.getTemperature(), loadedClimate.getHumidity()); // Устанавливаем загруженный климат
            Config.loadState(in);
            System.out.println("Состояние загружено из " + filename);
            return instance;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при загрузке состояния: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        // Верхняя граница
        res.append("+" + "-".repeat(width * 8) + "+\n");

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
                        res.append(GREEN + " [*HE*]" + RESET); // Травоядное
                    } else if (cell.getCurrentAnimal() instanceof Predator) {
                        res.append(RED + " [*PR*]" + RESET); // Хищник
                    } else if (cell.hasPlant() && !cell.hasAnimal()) {
                        res.append(YELLOW + String.format(" [*%dT*]", currentPlantsInCell) + RESET); // Растение
                    } else {
                        res.append(" [****]");
                    }
                } else {
                    res.append(" [****]");
                }
            }
            res.append("|\n");  // Правая граница
        }

        // Нижняя граница
        res.append("+" + "-".repeat(width * 8) + "+\n");

        return res.toString();
    }


}
