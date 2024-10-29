package org.andaevalexander;

import org.andaevalexander.map.EcosystemMap;

import java.util.Scanner;

import static org.andaevalexander.Colors.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите высоту карты: ");
        int height = 5;
        System.out.println("Введите ширину карты: ");
        int width = 5;
        EcosystemMap map = EcosystemMap.getInstance(height, width);
        System.out.println("Введите кол-во дней симуляции: ");
        int simulationDays = 1000;
        Ecosystem ecosystem = new Ecosystem(map);
        StringBuilder legend = new StringBuilder();
        legend.append("Легенда:\n")
                .append(GREEN + "[H]" + RESET + " - Травоядное\n")
                .append(RED + "[P]" + RESET + " - Хищник\n")
                .append(BLUE + "[W]" + RESET + " - Животное и растение\n")
                .append(YELLOW + "[T]" + RESET + " - Растение\n");
        System.out.println(legend);
        for (int i = 0; i < simulationDays; i++) {
            System.out.println("Ход " + (i+1));
            ecosystem.simulate();
            System.out.println("Конеца хода");
        }
    }
}