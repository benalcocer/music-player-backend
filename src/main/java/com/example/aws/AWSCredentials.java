package com.example.aws;

public class AWSCredentials {

    private final String accessKey;
    private final String secretKey;

    public AWSCredentials(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
