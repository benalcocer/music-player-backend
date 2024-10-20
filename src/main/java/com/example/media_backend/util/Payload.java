package com.example.media_backend.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class Payload {

    private final Map<String, Object> map;

    public Payload(Map<String, Object> map) {
        this.map = map != null ? map : Collections.emptyMap();
    }

    public Optional<String> getOptionalString(String key) {
        return Optional.ofNullable(getStringOrDefault(key, null));
    }

    public String getString(String key) {
        return getStringOrDefault(key, null);
    }

    public String getStringOrDefault(String key, String defaultValue) {
        Object objectValue = map.get(key);
        try {
            return objectValue != null ? (String) objectValue : defaultValue;
        } catch (ClassCastException e) {
            Logger.getGlobal().info(e.getMessage());
            return null;
        }
    }

    public List<String> getStringList(String key) {
        return getStringListOrDefault(key, null);
    }

    public List<String> getStringListOrDefault(String key, List<String> defaultList) {
        Object objectValue = map.get(key);
        try {
            return objectValue != null ? (List<String>) objectValue : defaultList;
        } catch (ClassCastException e) {
            Logger.getGlobal().info(e.getMessage());
            return null;
        }
    }

    public static boolean nonNull(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                return false;
            }
        }
        return true;
    }
}
