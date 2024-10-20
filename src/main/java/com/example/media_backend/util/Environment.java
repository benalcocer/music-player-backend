package com.example.media_backend.util;

import java.util.logging.Logger;

public enum Environment {
    APPLICATION_NAME,
    APPLICATION_PORT,
    JWT_SECRET_KEY,
    JWT_EXPIRATION_TIME,
    MONGO_DATABASE,
    MONGO_USER,
    MONGO_PASSWORD,
    MONGO_CLUSTER,
    MINIO_HOST,
    MINIO_BUCKET_ACCESS_KEY,
    MINIO_BUCKET_SECRET_KEY,
    SPRING_MAIL_HOST,
    SPRING_MAIL_PORT,
    SUPPORT_EMAIL,
    APP_PASSWORD;

    private EnvironmentValue environmentValue;

    static {
        for (Environment environment : values()) {
            environment.environmentValue = new EnvironmentValue(environment.name());
        }
    }

    public EnvironmentValue getEnvironmentValue() {
        return environmentValue;
    }

    public String getValue() {
        return environmentValue.getValue();
    }

    public static boolean isEnvironmentLoaded() {
        boolean retVal = true;
        for (Environment environment : values()) {
            if (!environment.getEnvironmentValue().isLoaded()) {
                Logger.getGlobal().severe("Missing environment variable: " + environment.name());
                retVal = false;
            }
        }
        return retVal;
    }
}
