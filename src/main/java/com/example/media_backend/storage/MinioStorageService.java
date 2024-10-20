package com.example.media_backend.storage;

import com.example.aws.AWSCredentials;
import com.example.aws.AWSSigner;
import com.example.media_backend.util.Environment;
import com.example.media_backend.util.EnvironmentManager;
import com.example.media_backend.util.RequestHelper;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class MinioStorageService {

    private final String host = EnvironmentManager.getInstance().get("MINIO_HOST").orElse("");

    private final MinioStorage minioStorage;

    public MinioStorageService() {
        minioStorage = new MinioStorage(new AWSCredentials(
            Environment.MINIO_BUCKET_ACCESS_KEY.getValue(),
            Environment.MINIO_BUCKET_SECRET_KEY.getValue()
        ));
    }

    public String getSignedURI(String link) {
        try {
            String urlString = host + "/" + link;
            URL url = RequestHelper.stringToURL(urlString);
            URI uri = RequestHelper.urlToURI(url);
            if (uri == null) {
                return null;
            }
            String signature = AWSSigner.getInstance().sign(HttpMethod.GET, minioStorage.getAWSCredentials(), uri.getRawPath());
            return uri + signature;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
