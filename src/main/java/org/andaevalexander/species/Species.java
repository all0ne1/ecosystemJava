package org.andaevalexander.species;


import org.andaevalexander.climate.Climate;
import org.andaevalexander.climate.Conditions;
import org.andaevalexander.map.Cell;
import org.andaevalexander.map.EcosystemMap;

import java.io.Serializable;

import static org.andaevalexander.Config.baseSpeciesSaturation;

public abstract class Species implements Serializable {
    protected String name;
    protected int age;
    protected int x;
    protected int y;
    protected int saturation = baseSpeciesSaturation;
    protected double energyConsumptionRate;
    protected Cell[][] map = EcosystemMap.getMap();
    public Species(String name, int age) {
        this.name = name;
        this.age = age;
        switch (Climate.getConditions()) {
            case GOOD:
                this.energyConsumptionRate = 1.2;
                break;
            case MODERATE:
                this.energyConsumptionRate = 1.0;
                break;
            case BAD:
                this.energyConsumptionRate = 0.7;
                break;
        }
    }

    public void updateConditions() {
        switch (Climate.getConditions()) {
            case GOOD:
                this.energyConsumptionRate = 1.2;
                break;
            case MODERATE:
                this.energyConsumptionRate = 1.0;
                break;
            case BAD:
                this.energyConsumptionRate = 0.7;
                break;
        }
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


}
