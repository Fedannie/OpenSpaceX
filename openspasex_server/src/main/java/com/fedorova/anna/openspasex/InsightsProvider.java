package com.fedorova.anna.openspasex;

import com.fedorova.anna.openspasex.model.Rocket;
import com.google.gson.*;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.util.*;
import java.util.stream.Stream;

public class InsightsProvider extends RestTemplate {
    private final static String URL = "https://api.spacexdata.com/v4";
    private final static String DATE_SUFFIX = "-01-01T00:00:00.000Z";

    private final Gson gson = new Gson();
    private final JsonParser parser = new JsonParser();

    public Rocket[] getAllRockets() {
        JsonObject[] response = getForEntity(URL + "/rockets", JsonObject[].class).getBody();
        if (response == null) return null;
        return Arrays
                .stream(response)
                .map(rocketJson -> {
                    Rocket rocket = gson.fromJson(rocketJson, Rocket.class);
                    JsonElement flickrImages = rocketJson.get("flickr_images");
                    if (flickrImages != null && flickrImages.getAsJsonArray().size() > 0) {
                        rocket.setImage(flickrImages.getAsJsonArray().get(0).getAsString());
                    }
                    return rocket;
                })
                .toArray(Rocket[]::new);
    }

    public long getCrewSize(Integer year) {
        JsonObject[] response;

        if (year != null) {
            Query body = postForEntity(URL + "/launches/query", getQuery(year), Query.class).getBody();

            if (body == null) return 0;
            response = body.docs;
        } else {
            response = getForEntity(URL + "/launches/past", JsonObject[].class).getBody();
        }

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

    private JsonObject getQuery(String populate) {
        return getQuery(null, populate);
    }

    private JsonObject getQuery(Integer year) {
        return getQuery(year, "");
    }

    private JsonObject getQuery(Integer year, String populate) {
        String query = "{";

        if (year != null) {
            LocalDate localDate = LocalDate.now();

            String secondDate;
            if (localDate.getYear() <= year) {
                secondDate = localDate.toString();
            } else {
                secondDate = (year + 1) + DATE_SUFFIX;
            }
            query += "   \"query\": {\n" +
                    "      \"date_utc\": {\n" +
                    "           \"$gte\": \"" + year + DATE_SUFFIX + "\",\n" +
                    "           \"$lte\": \"" + secondDate + "\"\n" +
                    "       }\n" +
                    "   },\n";
        }
        query += "   \"options\": {\n" +
                "       \"pagination\": false,\n" +
                "       \"populate\": [\n" +
                "           \"" + populate + "\"\n" +
                "       ]\n" +
                "    }\n" +
                "}";
        return parser.parse(query).getAsJsonObject();
    }

    private static class Query {
        private final JsonObject[] docs;

        public Query(JsonObject[] docs) {
            this.docs = docs;
        }
    }
}
