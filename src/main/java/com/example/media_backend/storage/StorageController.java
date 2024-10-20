package com.example.media_backend.storage;

import com.example.media_backend.model.Song;
import com.example.media_backend.song.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/storage")
public class StorageController {
    @Autowired
    private MinioStorageService minioStorageService;
    @Autowired
    private SongService songService;

    @GetMapping("/{id}")
    public ResponseEntity<String> getSignedURI(@PathVariable String id) {
        Song song = songService.getSong(id);
        String signedURI = song != null ? minioStorageService.getSignedURI(song.getLink()) : null;
        if (signedURI == null) {
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(signedURI, HttpStatus.OK);
    }
}
