package org.andaevalexander;

import org.andaevalexander.climate.Climate;
import org.andaevalexander.species.*;
import org.andaevalexander.map.Cell;
import org.andaevalexander.map.EcosystemMap;

import java.io.IOException;
import java.util.*;
import java.util.logging.*;

import static org.andaevalexander.Config.amountOfWaterAddedToWaterSources;

public class Ecosystem {
    private final EcosystemMap ecosystemMap;
    private final Cell[][] map;
    private final HashMap<List<Integer>, Cell> coordsToCellMap;
    public static final Logger LOGGER = Logger.getLogger(Ecosystem.class.getName());

    public Ecosystem(EcosystemMap ecosystemMap, int humidity, double temperature) {
        this.ecosystemMap = ecosystemMap;
        this.map = EcosystemMap.getMap();
        this.coordsToCellMap = new HashMap<>();
        Climate climate = Climate.getInstance(temperature, humidity);

        try {
            setupLogger();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupLogger() throws IOException {
        FileHandler fileHandler = new FileHandler("./logs/ecosystem_simulation_log_" + EcosystemMap.UUID_FILE + ".txt", true);
        fileHandler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(fileHandler);
        LOGGER.setUseParentHandlers(false);
    }

    public void simulate() {
        LOGGER.info("Starting new simulation iteration.");
        System.out.println(ecosystemMap);
        LOGGER.info(ecosystemMap.toString().replaceAll("\u001B\\[[;\\d]*m", ""));
        System.out.println("Вода распространяется\n");
        // Вода распространяется
        LOGGER.info("Вода распространяется");
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                EcosystemMap.spreadWater(x, y);
            }
        }

        // Растения растут
        LOGGER.info("Растения растут");
        System.out.println("Растения растут");
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                Cell currentCell = map[y][x];
                if (!currentCell.getPlants().isEmpty()) {
                    List<Plant> plantsCopy = new ArrayList<>(currentCell.getPlants());

                    for (Plant plant : plantsCopy) {
                        plant.consumeResources();
                        plant.grow();
                    }
                }
            }
        }
        System.out.println(ecosystemMap);
        LOGGER.info(ecosystemMap.toString().replaceAll("\u001B\\[[;\\d]*m", ""));
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                List<Integer> coords = Arrays.asList(y, x);
                if (map[y][x].getCurrentAnimal() != null) {
                    coordsToCellMap.put(coords, map[y][x]);
                }
            }
        }

        // Травоядные ходят
        LOGGER.info("Травоядные ходят");
        System.out.println("Травоядные ходят");
        coordsToCellMap.forEach((coord, cell) -> {
            Animal animal = cell.getCurrentAnimal();
            if (animal instanceof Herbivore) {
                animal.eat();
                animal.move();
                animal.reproduce();
            }
        });

        System.out.println(ecosystemMap);
        LOGGER.info(ecosystemMap.toString());
        // Хищники ходят
        LOGGER.info("Хищники ходят");
        coordsToCellMap.forEach((coord, cell) -> {
            Animal animal = cell.getCurrentAnimal();
            if (animal instanceof Predator) {
                animal.eat();
                animal.move();
                animal.reproduce();
            }
        });

        System.out.println(ecosystemMap);
        LOGGER.info(ecosystemMap.toString().replaceAll("\u001B\\[[;\\d]*m", ""));

        // Пополнение источников воды
        System.out.println("Пополнение источников воды");
        LOGGER.info("Пополнение источников воды");
        EcosystemMap.getWaterSourceMap().forEach((coord, waterSource) -> {
            waterSource.addWater(amountOfWaterAddedToWaterSources);
        });

        // Старение
        System.out.println("Старение...");
        LOGGER.info("Происходит старение");
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                Cell currentCell = map[y][x];

                // Старение всех растений в клетке
                if (!currentCell.getPlants().isEmpty()) {
                    List<Plant> plantsCopy = new ArrayList<>(currentCell.getPlants());
                    for (Plant plant : plantsCopy) {
                        plant.aging();
                    }
                }

                // Старение животного в клетке, если оно там есть
                Animal animal = currentCell.getCurrentAnimal();
                if (animal != null) {
                    animal.aging();
                }
            }
        }

        int totalWater = 0;
        int totalPlants = 0;
        int totalHerbivores = 0;
        int totalPredators = 0;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                Cell currentCell = map[y][x];
                totalWater += currentCell.getWaterCapacity();
                totalPlants += currentCell.getPlants().size();
                totalHerbivores += currentCell.getCurrentAnimal() instanceof Herbivore ? 1 : 0;
                totalPredators += currentCell.getCurrentAnimal() instanceof Predator ? 1 : 0;
            }
        }

        // Обновление условий у видов
        coordsToCellMap.forEach((coords, cell) -> {
            Species animal = (Species) cell.getCurrentAnimal();
            if (animal != null) animal.updateConditions();
            for (Plant plant : cell.getPlants()) {
                ((Species) plant).updateConditions();
            }
        });
        Climate.updateClimate();

        // Логгирование финальной статистики
        LOGGER.info("Final Statistics:");
        LOGGER.info("Total Water Capacity: " + totalWater);
        LOGGER.info("Total Plants: " + totalPlants);
        LOGGER.info("Total Herbivores: " + totalHerbivores);
        LOGGER.info("Total Predators: " + totalPredators);
        LOGGER.info("Climate: " + Climate.getInstance().toString());


        coordsToCellMap.clear();
        EcosystemMap.saveState();

        LOGGER.info("Simulation iteration complete.\n");
    }
}
