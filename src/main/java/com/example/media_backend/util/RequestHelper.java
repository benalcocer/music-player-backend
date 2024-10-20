package com.example.media_backend.util;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

public class RequestHelper {

    private RequestHelper() {

    }

    public static URL stringToURL(String urlString) {
        try {
            String decodedURL = URLDecoder.decode(urlString, "UTF-8");
            return new URL(decodedURL);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URI urlToURI(URL url) {
        if (url == null) {
            return null;
        }
        try {
            return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
