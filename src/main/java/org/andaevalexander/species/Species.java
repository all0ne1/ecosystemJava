package org.andaevalexander.species;


import org.andaevalexander.climate.Climate;
import org.andaevalexander.climate.Conditions;
import org.andaevalexander.map.Cell;
import org.andaevalexander.map.EcosystemMap;

import java.io.Serializable;

public abstract class Species implements Serializable {
    protected String name;
    protected int age;
    protected int x;
    protected int y;
    protected int saturation = 100;
    protected double energyConsumptionRate;
    protected int energyToGrowth;
    protected Cell[][] map = EcosystemMap.getMap();
    public Species(String name, int age) {
        this.name = name;
        this.age = age;
        switch (Climate.getConditions()) {
            case Conditions.GOOD:
                this.energyConsumptionRate = 1.2;
                break;
            case Conditions.MODERATE:
                this.energyConsumptionRate = 1.0;
                break;
            case Conditions.BAD:
                this.energyConsumptionRate = 0.7;
                break;
        }
        this.energyToGrowth = (int) (100 * energyConsumptionRate);
    }

    public void setEnergyConsumptionRate(double energyConsumptionRate) {
        this.energyConsumptionRate = energyConsumptionRate;
    }

    public void setEnergyToGrowth(int energyToGrowth) {
        this.energyToGrowth = energyToGrowth;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


}
