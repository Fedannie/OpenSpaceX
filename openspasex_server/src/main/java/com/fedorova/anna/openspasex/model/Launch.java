package com.fedorova.anna.openspasex.model;

public class Launch {
    private final String id;
    private final String name;
    private final Rocket rocket;
    private final String date;
    private final boolean success;
    private final String logo;
    private final String description;
    private final int crewCnt;

    public Launch(
            String id,
            String name,
            Rocket rocket,
            String date,
            boolean success,
            String logo,
            String description,
            int crewCnt) {
        this.id = id;
        this.name = name;
        this.rocket = rocket;
        this.date = date;
        this.success = success;
        this.logo = logo;
        this.description = description;
        this.crewCnt = crewCnt;
    }
}
