package com.example.media_backend.playlist;

import com.example.media_backend.model.Playlist;
import com.example.media_backend.model.Song;
import com.example.media_backend.model.User;
import com.example.media_backend.user.UserService;
import com.example.media_backend.util.Payload;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Playlist> allPlaylists() {
        return playlistRepository.findAll();
    }

    public List<Playlist> getUserPlaylists(String userId) {
        Optional<User> user = userService.getExistingUser(userId);
        if (user.isEmpty()) {
            return null;
        }
        return playlistRepository.findAllById(user.get().getPlaylistIds());
    }

    public Playlist getPlaylist(String id) {
        return playlistRepository.findById(id).orElse(null);
    }

    public void deletePlaylist(String id) {
        playlistRepository.deleteById(id);
    }

    public Playlist savePlaylist(Payload payload) {
        return savePlaylist(Playlist.fromPayload(payload));
    }

    public Playlist savePlaylist(Playlist newPlaylist) {
        if (newPlaylist == null) {
            return null;
        }
        Playlist playlist = playlistRepository.save(newPlaylist);
        mongoTemplate.update(Song.class)
            .matching(Criteria.where("id").is(playlist.getId()))
            .apply(new Update().push("playlist").value(playlist))
            .first();
        return playlist;
    }
}
