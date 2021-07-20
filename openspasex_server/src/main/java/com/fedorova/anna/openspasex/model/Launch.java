package com.fedorova.anna.openspasex.model;

import com.fedorova.anna.openspasex.utils.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;

public class Launch {
    private final String id;
    private final String name;
    private Rocket rocket;
    @SerializedName(value = "date_utc")
    private final String date;
    private String logo;
    private final String description;
    @SerializedName(value = "crew_cnt")
    private int crewCnt;

    public Launch(
            String id,
            String name,
            Rocket rocket,
            String date,
            String logo,
            String description,
            int crewCnt) {
        this.id = id;
        this.name = name;
        this.rocket = rocket;
        this.date = date;
        this.logo = logo;
        this.description = description;
        this.crewCnt = crewCnt;
    }

    public void setRocket(Rocket rocket) {
        this.rocket = rocket;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setCrewCnt(int crewCnt) {
        this.crewCnt = crewCnt;
    }

    public static Launch decode(JsonObject jsonObject) {
        if (jsonObject == null) return null;

        Launch launch = new Gson().fromJson(jsonObject, Launch.class);
        launch.setRocket(Rocket.decode(jsonObject.getAsJsonObject("rocket")));

        JsonPrimitive image = JsonUtils.getNestedPrimitive(jsonObject, "links", "patch", "large");
        if (image != null) {
            launch.setLogo(image.getAsString());
        }

        JsonArray crew = jsonObject.getAsJsonArray("crew");
        if (crew != null) launch.setCrewCnt(crew.size());

        return launch;
    }
}
