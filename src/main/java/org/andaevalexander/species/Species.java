package org.andaevalexander.species;


import org.andaevalexander.map.Cell;
import org.andaevalexander.map.EcosystemMap;

public abstract class Species {
    protected String name;
    protected int age;
    protected int x;
    protected int y;
    protected int saturation = 100;
    Cell[][] map = EcosystemMap.getMap();
    public Species(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


}
