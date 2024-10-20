package com.example.media_backend;

import com.example.media_backend.util.Environment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class MediaBackendApplication {

	public static void main(String[] args) {
		if (Environment.isEnvironmentLoaded()) {
			SpringApplication.run(MediaBackendApplication.class, args);
		} else {
			System.exit(1);
		}
	}
}
