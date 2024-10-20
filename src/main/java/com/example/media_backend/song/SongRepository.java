package com.example.media_backend.song;

import com.example.media_backend.model.Song;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SongRepository extends MongoRepository<Song, String> {
    void deleteById(String id);
}

