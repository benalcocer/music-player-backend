package com.example.media_backend.util;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.Optional;

public class EnvironmentManager {

    private final Dotenv dotenv = Dotenv.load();

    private EnvironmentManager() {

    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(dotenv.get(key));
    }

    private static class EnvironmentSingleton {
        private static final EnvironmentManager INSTANCE = new EnvironmentManager();
    }

    public static EnvironmentManager getInstance() {
        return EnvironmentSingleton.INSTANCE;
    }
}
