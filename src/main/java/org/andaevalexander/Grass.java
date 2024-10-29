package org.andaevalexander;

public class Grass extends Species implements Plant{
    public Grass(String name, int age, double growthRate, EcosystemMap ecosystemMap, int x, int y) {
        super(name, age, growthRate, ecosystemMap);
    }

    @Override
    public void grow() {

    }

    @Override
    public void consumeResources() {

    }

    @Override
    public int getCostOfEating() {
        return 0;
    }
}
