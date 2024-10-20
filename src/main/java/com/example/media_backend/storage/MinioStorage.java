package com.example.media_backend.storage;

import com.example.aws.AWSCredentials;

public class MinioStorage {

    private final AWSCredentials awsCredentials;

    public MinioStorage(AWSCredentials awsCredentials) {
        this.awsCredentials = awsCredentials;
    }

    protected final AWSCredentials getAWSCredentials() {
        return awsCredentials;
    }
}
