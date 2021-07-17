package com.fedorova.anna.openspasex.model;

import com.google.gson.annotations.SerializedName;

public class Rocket {
    private final String id;
    private final String name;
    private String image;
    private final String description;
    @SerializedName(value = "cost_per_launch")
    private final int costPerLaunch;

    public Rocket(String id, String name, String image, String description, int costPerLaunch) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.costPerLaunch = costPerLaunch;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
