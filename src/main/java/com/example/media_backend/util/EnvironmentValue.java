package com.example.media_backend.util;

import java.util.Optional;

public class EnvironmentValue {

    private final String name;
    private final String value;
    private final boolean isLoaded;

    public EnvironmentValue(String name) {
        this.name = name;
        Optional<String> valueOptional = EnvironmentManager.getInstance().get(name);
        this.value = valueOptional.orElse("");
        this.isLoaded = valueOptional.isPresent();
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isLoaded() {
        return isLoaded;
    }
}
