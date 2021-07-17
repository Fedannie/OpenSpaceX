package com.fedorova.anna.openspasex.model;

public class Rocket {
    private final String id;
    private final String name;
    private final String image;
    private final String description;
    private final int flightCnt;
    private final int costPerLaunch;

    public Rocket(String id, String name, String image, String description, int flightCnt, int costPerLaunch) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.flightCnt = flightCnt;
        this.costPerLaunch = costPerLaunch;
    }
}
