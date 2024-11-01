package org.andaevalexander;

import org.andaevalexander.climate.Climate;
import org.andaevalexander.map.EcosystemMap;
import org.andaevalexander.utils.Utils;

import java.util.List;
import java.util.Scanner;

import static org.andaevalexander.utils.Colors.*;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)){
            List<String> availableSimulations = Utils.getAvailableSimulations();
            EcosystemMap map = Utils.loadOrCreateNewSimulation(availableSimulations, sc);
            System.out.println("Введите кол-во дней симуляции: ");
            int simulationDays = sc.nextInt();
            Ecosystem ecosystem;
            if (Climate.getInstance() == null){
                int humidity = -1;
                while (humidity < 0 || humidity > 100) {
                    System.out.println("Введите влажность (0-100): ");
                    humidity = sc.nextInt();
                }
                double temperature = -200;
                while (temperature < -50 || temperature > 70) {
                    System.out.println("Введите температуру (Градусы Цельсия -50 - 70): ");
                    temperature = sc.nextDouble();
                }
                ecosystem = new Ecosystem(map, humidity, temperature);
            } else{
                ecosystem = new Ecosystem(map, Climate.getHumidity(), Climate.getTemperature());
            }
            printLegend();
            for (int i = 0; i < simulationDays; i++) {
                System.out.println("Ход " + (i+1));
                ecosystem.simulate();
                System.out.println("Конец хода");
            }
        }
    }

    private static void printLegend(){
        StringBuilder legend = new StringBuilder();
        legend.append("Легенда:\n")
                .append(GREEN + "[HE]" + RESET + " - Травоядное\n")
                .append(RED + "[P]" + RESET + " - Хищник\n")
                .append(GREEN + "[H,T]" + RESET + " - Травоядное и растение\n")
                .append(RED + "[P,T]" + RESET + " - Хищник и растение\n")
                .append(YELLOW + "[T]" + RESET + " - Растение\n");
        System.out.println(legend);
    }
}