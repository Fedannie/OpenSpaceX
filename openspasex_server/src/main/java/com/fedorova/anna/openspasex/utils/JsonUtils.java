package com.fedorova.anna.openspasex.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonUtils {
    public static JsonPrimitive getNestedPrimitive(JsonObject object, String... values) {
        if (object == null || object.isJsonNull()) return null;
        JsonElement innerObject = object.deepCopy();
        for (String value : values) {
            innerObject = innerObject.getAsJsonObject().get(value);
            if (innerObject == null || innerObject.isJsonNull()) return null;
        }
        return innerObject.getAsJsonPrimitive();
    }
}
