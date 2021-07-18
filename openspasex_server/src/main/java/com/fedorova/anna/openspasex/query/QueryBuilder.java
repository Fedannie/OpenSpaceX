package com.fedorova.anna.openspasex.query;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QueryBuilder {
    private final static String DATE_SUFFIX = "-01-01T00:00:00.000Z";

    private String fromDate;
    private String toDate;
    private int limit = -1;
    private String sortBy;
    private String populate;
    private boolean pagination = true;

    public QueryBuilder addFromDate(LocalDate date) {
        fromDate = date.toString();
        return this;
    }

    public QueryBuilder addFromDate(int year) {
        fromDate = year + DATE_SUFFIX;
        return this;
    }

    public QueryBuilder addToDate(LocalDateTime date) {
        toDate = date.format(DateTimeFormatter.ISO_DATE_TIME);
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

    public QueryBuilder addPopulate(String populate) {
        this.populate = populate;
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
        if (hasFromDate || hasToDate) {
            JsonObject innerQuery = new JsonObject();
            JsonObject date = new JsonObject();
            if (hasFromDate) date.addProperty("$gte", fromDate);
            if (hasToDate) date.addProperty("$lte", toDate);
            innerQuery.add("date_utc", date);
            query.add("query", innerQuery);
        }

        JsonObject options = new JsonObject();
        options.addProperty("pagination", pagination);

        if (populate != null && !populate.isEmpty()) {
            JsonArray populateArray = new JsonArray();
            populateArray.add(new JsonPrimitive(populate));
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
