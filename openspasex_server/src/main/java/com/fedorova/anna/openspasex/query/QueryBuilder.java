package com.fedorova.anna.openspasex.query;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.time.Instant;
import java.util.Arrays;

public class QueryBuilder {
    private final static String DATE_SUFFIX = "-01-01T00:00:00.000Z";

    private String fromDate;
    private String toDate;
    private int limit = -1;
    private String sortBy;
    private String[] populates;
    private boolean pagination = true;
    private boolean upcoming = false;

    public QueryBuilder addUpcoming() {
        upcoming = true;
        return this;
    }

    public QueryBuilder addFromDate(Instant date) {
        fromDate = date.toString();//format(DateTimeFormatter.ISO_DATE_TIME);
        return this;
    }

    public QueryBuilder addFromDate(int year) {
        fromDate = year + DATE_SUFFIX;
        return this;
    }

    public QueryBuilder addToDate(Instant date) {
        toDate = date.toString();//date.format(DateTimeFormatter.ISO_DATE_TIME);
        return this;
    }

    public QueryBuilder addToDate(int year) {
        toDate = year + DATE_SUFFIX;
        return this;
    }

    public QueryBuilder addLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public QueryBuilder addSortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    public QueryBuilder addPopulates(String... populates) {
        this.populates = populates;
        return this;
    }

    public QueryBuilder addPagination(boolean pagination) {
        this.pagination = pagination;
        return this;
    }

    public JsonObject build() {
        JsonObject query = new JsonObject();

        boolean hasFromDate = (fromDate != null && !fromDate.isEmpty());
        boolean hasToDate = (toDate != null && !toDate.isEmpty());

        JsonObject innerQuery = new JsonObject();
        if (hasFromDate || hasToDate) {
            JsonObject date = new JsonObject();
            if (hasFromDate) date.addProperty("$gte", fromDate);
            if (hasToDate) date.addProperty("$lte", toDate);
            innerQuery.add("date_utc", date);
        }
        innerQuery.addProperty("upcoming", upcoming);
        query.add("query", innerQuery);

        JsonObject options = new JsonObject();
        options.addProperty("pagination", pagination);

        if (populates != null && populates.length > 0) {
            JsonArray populateArray = new JsonArray();
            Arrays.stream(populates).forEach(populate -> populateArray.add(new JsonPrimitive(populate)));
            options.add("populate", populateArray);
        }

        if (limit > 0) {
            options.addProperty("limit", limit);
        }

        if (sortBy != null && !sortBy.isEmpty()) {
            JsonObject sorting = new JsonObject();
            sorting.addProperty(sortBy, "asc");
            options.add("sort", sorting);
        }

        query.add("options", options);
        return query;
    }
}
