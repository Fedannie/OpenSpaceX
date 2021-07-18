package com.fedorova.anna.openspasex;

import com.fedorova.anna.openspasex.model.Launch;
import com.fedorova.anna.openspasex.model.Rocket;
import com.fedorova.anna.openspasex.query.QueryBuilder;
import com.google.gson.*;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.util.*;
import java.util.stream.Stream;

public class InsightsProvider extends RestTemplate {
    private final static String URL = "https://api.spacexdata.com/v4";

    public Launch getNextLaunch() {
        JsonObject query = new QueryBuilder()
                .addFromDate(LocalDate.now())
                .addLimit(1)
                .addSortBy("date_unix")
                .addPagination(true)
                .addPopulate("rocket")
                .build();
        Query body = postForObject(URL + "/launches/query", query, Query.class);
        if (body == null) return null;
        return Launch.decode(body.docs[0]);
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
        JsonObject[] response = getForObject(URL + "/rockets", JsonObject[].class);
        if (response == null) return null;
        return Arrays
                .stream(response)
                .map(Rocket::decode)
                .toArray(Rocket[]::new);
    }

    public long getCrewSize(Integer year) {
        JsonObject[] response = getAllLaunches(year);
        if (response == null) return 0;

        return Arrays
                .stream(response)
                .map(launch -> {
                    ArrayList<String> ids = new ArrayList<>();
                    launch.getAsJsonArray("crew").forEach(id -> ids.add(id.getAsString()));
                    return ids.toArray();
                })
                .flatMap(Stream::of)
                .distinct()
                .count();
    }

    private JsonObject[] getAllLaunches(Integer year) {
        if (year == null) return getForObject(URL + "/launches/past", JsonObject[].class);

        QueryBuilder queryBuilder = new QueryBuilder();

        LocalDateTime localDate = LocalDateTime.now();
        if (localDate.getYear() <= year) {
            queryBuilder.addToDate(localDate);
        } else {
            queryBuilder.addToDate(year + 1);
        }
        JsonObject query = queryBuilder
                .addFromDate(year)
                .addPagination(false)
                .build();

        Query body = postForObject(URL + "/launches/query", query, Query.class);
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
