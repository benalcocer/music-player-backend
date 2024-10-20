package com.example.media_backend.playlist;

import com.example.media_backend.model.Playlist;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlaylistRepository extends MongoRepository<Playlist, String> {

}
