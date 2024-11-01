package org.andaevalexander;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

import static org.andaevalexander.map.EcosystemMap.SAVE_FILE;

public class Config implements Serializable {
    private static Config instance;
    public static int maxWaterInCell;
    public static int maxPlantInCell;
    public static int startAmountOfPredators;
    public static int startAmountOfHerbivore;
    public static int minPlantsWaterAmount;
    public static int baseSpeciesSaturation;
    public static int predatorsHuntChance;
    public static int rangeOfPredatorsAge;
    public static int rangeOfHerbivoreAge;
    public static int rangeOfPlantsAge;
    public static int predatorsLifeExpectancy;
    public static int herbivoreLifeExpectancy;
    public static int plantsLifeExpectancy;
    public static int saturationNeedToReproducePredators;
    public static int saturationNeedToReproduceHerbivore;
    public static int saturationNeedToGrowthPlants;
    public static int plantsSaturationPerTurns;
    public static int numOfWaterSources;
    public static int amountOfWaterAddedToWaterSources;

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    private Config() {
        initializeConfig();
    }

    public static void initializeConfig() {
        Scanner sc = new Scanner(System.in);
        maxWaterInCell = getIntInput(sc, "Введите максимальное количество воды в клетке: ");
        maxPlantInCell = getIntInput(sc, "Введите максимальное количество растений в клетке: ");
        startAmountOfPredators = getIntInput(sc, "Введите n стартовых хищников: ");
        startAmountOfHerbivore = getIntInput(sc, "Введите n стартовых травоядных: ");
        minPlantsWaterAmount = getIntInput(sc, "Введите сколько воды содержится в растениях по умолчанию: ");
        baseSpeciesSaturation = getIntInput(sc, "Введите базовое значение сытости у всех видов: ");
        predatorsHuntChance = getIntInput(sc, "Введите шанс на охоту у хищников (%): ");
        rangeOfPredatorsAge = getIntInput(sc, "Введите макс. стартовый возраст хищников: ");
        rangeOfHerbivoreAge = getIntInput(sc, "Введите макс. стартовый возраст травоядных: ");
        rangeOfPlantsAge = getIntInput(sc, "Введите макс. стартовый возраст растений: ");
        predatorsLifeExpectancy = getIntInput(sc, "Введите макс. возраст хищников: ");
        herbivoreLifeExpectancy = getIntInput(sc, "Введите макс. возраст травоядных: ");
        plantsLifeExpectancy = getIntInput(sc, "Введите макс. возраст растений: ");
        saturationNeedToReproducePredators = getIntInput(sc, "Введите значение сытости для размножения хищников: ");
        saturationNeedToReproduceHerbivore = getIntInput(sc, "Введите значение сытости для размножения травоядных: ");
        saturationNeedToGrowthPlants = getIntInput(sc, "Введите значения сытости для роста растений: ");
        plantsSaturationPerTurns = getIntInput(sc, "Введите получение сытости растениями за ход: ");
        numOfWaterSources = getIntInput(sc, "Введите кол-во источников воды: ");
        amountOfWaterAddedToWaterSources = getIntInput(sc, "Введите на сколько источники воды пополняются: ");
        System.out.println("Успешно введены конфигурационные файлы");

    }

    private static int getIntInput(Scanner sc, String prompt) {
        int input = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.print(prompt);
            try {
                input = sc.nextInt();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Ошибка ввода. Пожалуйста, введите целое число.");
                sc.next();
            }
        }
        return input;
    }

    public static void saveState(ObjectOutputStream out) throws IOException {
        out.writeObject(getInstance());
        System.out.println("Конфигурация сохранена в " + SAVE_FILE);
    }

    // Method to load Config state
    public static void loadState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        instance = (Config) in.readObject();
        System.out.println("Конфигурация загружена из " + SAVE_FILE);
    }

}

