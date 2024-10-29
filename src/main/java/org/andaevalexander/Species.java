package org.andaevalexander;

public abstract class Species {
    protected String name;
    protected int age;
    protected double growthRate;
    protected int x;
    protected int y;
    protected final Cell[][] map;
    protected int saturation = 100;
    public Species(String name, int age, double growthRate, EcosystemMap ecosystemMap) {
        this.name = name;
        this.age = age;
        this.growthRate = growthRate;
        map = ecosystemMap.getMap();
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getGrowthRate() {
        return growthRate;
    }
}
