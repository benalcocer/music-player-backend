package com.example.aws;

import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.http.HttpMethod;

public class AWSSigner {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static byte[] HMAC_SHA1(String key, String input) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            return mac.doFinal(input.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String UTF8Encoding(String input) {
        return new String(input.getBytes(), StandardCharsets.UTF_8);
    }

    private static String UTF8Encoding(byte[] input) {
        return new String(input, StandardCharsets.UTF_8);
    }

    private static String getStringToSign(HttpMethod httpMethod, long expireTime, String path) {
        return httpMethod.name() + "\n" +
            "\n" +
            "\n" +
            expireTime + "\n" +
            path;
    }

    public String sign(HttpMethod httpMethod, AWSCredentials awsCredentials, String path) throws Exception {
        long expireTime = Instant.now().getEpochSecond() + 60;
        String stringToSign = getStringToSign(httpMethod, expireTime, path);
        String signature = URLEncoder.encode(Base64.getEncoder().encodeToString(HMAC_SHA1(awsCredentials.getSecretKey(), UTF8Encoding(stringToSign))), StandardCharsets.UTF_8);
        return  "?AWSAccessKeyId=" + awsCredentials.getAccessKey()
            + "&Expires=" + expireTime
            + "&Signature=" + signature;
    }

    private static class AWSUtilSingleton {
        private static final AWSSigner INSTANCE = new AWSSigner();
    }

    public static AWSSigner getInstance() {
        return AWSUtilSingleton.INSTANCE;
    }
}
