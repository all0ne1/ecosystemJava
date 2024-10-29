package org.andaevalexander;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите высоту карты: ");
        int height = 5;
        System.out.println("Введите ширину карты: ");
        int width = 5;
        EcosystemMap map = EcosystemMap.getInstance(height, width);
        System.out.println("Введите кол-во дней симуляции: ");
        int simulationDays = 5;
        Ecosystem ecosystem = new Ecosystem(map);
        while (simulationDays > 0) {
            ecosystem.simulate();
            simulationDays--;
        }
    }
}