package org.andaevalexander.utils;

import org.andaevalexander.Config;
import org.andaevalexander.map.EcosystemMap;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Utils {
    public static List<String> getAvailableSimulations(){
        File directory = new File("./savedSimulations");
        List<String> availableSimulations;
        availableSimulations = Arrays.stream(directory.listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .filter((name) -> name.endsWith("txt"))
                .collect(Collectors.toList());
        System.out.println("Доступные симуляции: ");
        for (int i = 0; i < availableSimulations.size(); i++) {
            System.out.println((i + 1) + ". " + availableSimulations.get(i));
        }
        System.out.println((availableSimulations.size() + 1) + ". Создать новую симуляцию.");
        return availableSimulations;
    }

    public static EcosystemMap loadOrCreateNewSimulation(List<String> availableSimulations, Scanner sc){
        System.out.println("Выберите опцию: ");
        int choice = sc.nextInt();
        if (choice > 0 && choice <= availableSimulations.size()) {
            return EcosystemMap.loadState(availableSimulations.get(choice - 1));
        } else {
            return createNewSimulation(sc);
        }
    }

    private static EcosystemMap createNewSimulation(Scanner sc){
        System.out.println("Введите высоту и ширину новой карты");
        int height = sc.nextInt();
        int width = sc.nextInt();
        Config.getInstance();
        return EcosystemMap.getInstance(height, width);
    }
}
