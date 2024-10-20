package com.example.media_backend.playlist;

import com.example.media_backend.model.Playlist;
import com.example.media_backend.util.Payload;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/playlists")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;

    @GetMapping
    public ResponseEntity<List<Playlist>> getAllPlaylists() {
        return new ResponseEntity<>(playlistService.allPlaylists(), HttpStatus.OK);
    }

    @GetMapping("/from-user/{id}")
    public ResponseEntity<List<Playlist>> getPlaylistSongs(@PathVariable String id) {
        List<Playlist> playlists = playlistService.getUserPlaylists(id);
        return new ResponseEntity<>(playlists, playlists != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getPlaylist(@PathVariable String id) {
        Playlist playlist = playlistService.getPlaylist(id);
        return new ResponseEntity<>(playlist, playlist != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping("/save")
    public ResponseEntity<Playlist> savePlaylist(@RequestBody Map<String, Object> payload) {
        Playlist playlist = playlistService.savePlaylist(new Payload(payload));
        return new ResponseEntity<>(playlist, playlist != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Playlist> deletePlaylist(@PathVariable String id) {
        Playlist playlist = playlistService.getPlaylist(id);
        if (playlist != null) {
            playlistService.deletePlaylist(id);
        }
        return new ResponseEntity<>(playlist, playlist != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
