package com.fedorova.anna.openspasex.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class Rocket {
    private final String id;
    private final String name;
    private String image;
    private final String description;
    @SerializedName(value = "cost_per_launch")
    private final long costPerLaunch;

    public Rocket(String id, String name, String image, String description, long costPerLaunch) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.costPerLaunch = costPerLaunch;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public long getCostPerLaunch() {
        return costPerLaunch;
    }

    public static Rocket decode(JsonObject jsonObject) {
        if (jsonObject == null) return null;
        Rocket rocket = new Gson().fromJson(jsonObject, Rocket.class);
        JsonElement flickrImages = jsonObject.get("flickr_images");
        if (flickrImages != null && !flickrImages.isJsonNull() && flickrImages.getAsJsonArray().size() > 0) {
            rocket.setImage(flickrImages.getAsJsonArray().get(0).getAsString());
        }
        return rocket;
    }
}
