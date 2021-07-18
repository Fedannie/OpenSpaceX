package com.fedorova.anna.openspasex.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonUtils {
    public static JsonPrimitive getNestedPrimitive(JsonObject object, String... values) {
        JsonObject innerObject = object.deepCopy();
        for (int i = 0; i < values.length - 1; i++) {
             innerObject = innerObject.getAsJsonObject(values[i]);
             if (innerObject == null) return null;
        }
        return innerObject.getAsJsonPrimitive(values[values.length - 1]);
    }
}
