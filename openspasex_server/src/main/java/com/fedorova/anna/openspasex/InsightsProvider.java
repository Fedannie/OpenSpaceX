package com.fedorova.anna.openspasex;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.util.*;
import java.util.stream.Stream;

public class InsightsProvider extends RestTemplate {
    private final static String URL = "https://api.spacexdata.com/v4";
    private final static String DATE_SUFFIX = "-01-01T00:00:00.000Z";

    private final Gson gson = new Gson();
    private final JsonParser parser = new JsonParser();

    public long getCrewSize(Integer year) {
        Object[] response;

        if (year != null) {
            Query body = postForEntity(URL + "/launches/query", getQuery(year), Query.class).getBody();

            if (body == null) return 0;
            response = body.docs;
        } else {
            response = getForEntity(URL + "/launches/past", Object[].class).getBody();
        }

        if (response == null) return 0;
        return Arrays
                .stream(response)
                .map(launch -> {
                    JsonObject jsonObject = parser.parse(gson.toJson(launch)).getAsJsonObject();
                    ArrayList<String> ids = new ArrayList<>();
                    jsonObject.getAsJsonArray("crew").forEach(id -> ids.add(id.getAsString()));
                    return ids.toArray();
                })
                .flatMap(Stream::of)
                .distinct()
                .count();
    }

    private JsonObject getQuery(Integer year) {
        LocalDate localDate = LocalDate.now();

        String secondDate;
        if (localDate.getYear() <= year) {
            secondDate = localDate.toString();
        } else {
            secondDate = (year + 1) + DATE_SUFFIX;
        }

        String query = "{" +
                "   \"query\": {\n" +
                "      \"date_utc\": {\n" +
                "           \"$gte\": \"" + year + DATE_SUFFIX + "\",\n" +
                "           \"$lte\": \"" + secondDate + "\"\n" +
                "       }\n" +
                "   },\n" +
                "   \"options\": {\n" +
                "       \"pagination\": false\n" +
                "    }\n" +
                "}";
        return parser.parse(query).getAsJsonObject();
    }

    private static class Query {
        private final Object[] docs;

        public Query(Object[] docs) {
            this.docs = docs;
        }
    }
}
