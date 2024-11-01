package org.andaevalexander;

import org.andaevalexander.map.EcosystemMap;

import java.io.IOException;
import java.util.Scanner;

import static org.andaevalexander.Colors.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EcosystemMap.loadState("save.txt");
        EcosystemMap map = EcosystemMap.getInstance();
        System.out.println("Введите кол-во дней симуляции: ");
        int simulationDays = 10;
        Ecosystem ecosystem = new Ecosystem(map, 50, 17 );
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