package com.fedorova.anna.openspasex;

import com.fedorova.anna.openspasex.model.Launch;
import com.fedorova.anna.openspasex.model.Rocket;
import com.fedorova.anna.openspasex.query.QueryBuilder;
import com.fedorova.anna.openspasex.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class InsightsProvider {
    private final static String URL = "https://api.spacexdata.com";
    private final static String OLD_VERSION = "/v4";
    private final static String NEW_VERSION = "/v5";
    private final RestTemplate restTemplate = new RestTemplate();

    public Launch getNextLaunch() {
        JsonObject query = new QueryBuilder()
                .addLimit(1)
                .addUpcoming()
                .addSortBy("flight_number")
                .addPagination(true)
                .addPopulates("rocket")
                .build();
        Query body = restTemplate.postForObject(URL + NEW_VERSION + "/launches/query", query, Query.class);
        if (body == null) return null;
        return Launch.decode(body.docs[0]);
    }

    public long getLaunchedMass(Integer year) {
        JsonObject[] response = getAllLaunches(year, "rocket", "payloads");
        if (response == null) return 0;
        return Arrays
                .stream(response)
                .mapToLong(launch -> {
                    long takeOffMass = 0;

                    JsonPrimitive rocketMass = JsonUtils.getNestedPrimitive(launch, "rocket", "mass", "kg");
                    if (rocketMass != null) takeOffMass += rocketMass.getAsLong();
                    JsonPrimitive firstStageFuel = JsonUtils.getNestedPrimitive(launch, "rocket", "first_stage", "fuel_amount_tons");
                    if (firstStageFuel != null) takeOffMass += firstStageFuel.getAsLong();
                    JsonPrimitive secondStageFuel = JsonUtils.getNestedPrimitive(launch, "rocket", "second_stage", "fuel_amount_tons");
                    if (secondStageFuel != null) takeOffMass += secondStageFuel.getAsLong();

                    JsonArray payloads = launch.getAsJsonArray("payloads");
                    if (payloads != null) {
                        for (int i = 0; i < payloads.size(); i++) {
                            JsonElement massJson = payloads.get(i).getAsJsonObject().get("mass_kg");
                            if (massJson != null && !massJson.isJsonNull()) takeOffMass += massJson.getAsLong();
                        }
                    }
                    return takeOffMass;
                })
                .sum();
    }

    public double getSuccessRate(Integer year) {
        JsonObject[] response = getAllLaunches(year);
        if (response == null) return 0;

        return Arrays
                .stream(response)
                .mapToInt(launch -> launch.get("success").getAsBoolean() ? 1 : 0)
                .average()
                .orElse(0);
    }

    public Rocket[] getAllRockets() {
        JsonObject[] response = restTemplate.getForObject(URL + OLD_VERSION + "/rockets", JsonObject[].class);
        if (response == null) return null;
        return Arrays
                .stream(response)
                .map(Rocket::decode)
                .toArray(Rocket[]::new);
    }

    public int getLaunchesCnt(Integer year) {
        JsonObject[] response = getAllLaunches(year);
        return (response == null) ? 0 : response.length;
    }

    public long getCost(Integer year) {
        JsonObject[] response = getAllLaunches(year, "rocket");
        if (response == null) return 0;

        return Arrays
                .stream(response)
                .map(launch -> {
                    JsonPrimitive cost = JsonUtils.getNestedPrimitive(launch, "rocket", "cost_per_launch");
                    return cost == null ? 0L : cost.getAsLong();
                })
                .reduce(0L, Long::sum);
    }

    public long getCrewSize(Integer year) {
        JsonObject[] response = getAllLaunches(year);
        if (response == null) return 0;

        return Arrays
                .stream(response)
                .map(launch -> {
                    ArrayList<String> ids = new ArrayList<>();
                     launch.getAsJsonArray("crew").forEach(crewMember ->
                             ids.add(crewMember
                                     .getAsJsonObject()
                                     .getAsJsonPrimitive("crew")
                                     .getAsString())
                     );
                    return ids.toArray();
                })
                .flatMap(Stream::of)
                .distinct()
                .count();
    }

    private JsonObject[] getAllLaunches(Integer year, String... populates) {
        if (year == null && populates.length == 0) {
            return restTemplate.getForObject(URL + NEW_VERSION + "/launches/past", JsonObject[].class);
        }

        JsonObject query = new QueryBuilder()
                .addDates(year)
                .addPagination(false)
                .addPopulates(populates)
                .build();

        Query body = restTemplate.postForObject(URL + NEW_VERSION + "/launches/query", query, Query.class);
        if (body == null) return null;
        return body.docs;
    }

    private static class Query {
        private final JsonObject[] docs;

        public Query(JsonObject[] docs) {
            this.docs = docs;
        }
    }
}
