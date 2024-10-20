package com.example.media_backend.song;

import com.example.media_backend.model.Song;
import com.example.media_backend.util.Payload;
import java.net.URLDecoder;
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
@RequestMapping("/api/v1/songs")
public class SongController {
    @Autowired
    private SongService songService;

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        return new ResponseEntity<>(songService.allSongs(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSong(@PathVariable String id) {
        Song song = songService.getSong(id);
        if (song == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(song, HttpStatus.OK);
        }
    }

    @GetMapping("/from-playlist/{id}")
    public ResponseEntity<List<Song>> getPlaylistSongs(@PathVariable String id) {
        List<Song> songs = songService.getPlaylistSongs(id);
        if (songs == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(songs, HttpStatus.OK);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Song> saveSong(@RequestBody Map<String, Object> payload) {
        Song song = songService.saveSong(new Payload(payload));
        return new ResponseEntity<>(song, song != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Song> deleteSong(@PathVariable String id) {
        Song song = songService.getSong(id);
        if (song != null) {
            songService.deleteSong(id);
        }
        return new ResponseEntity<>(song, song != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<List<Song>> searchSongs(@PathVariable String id) {
        try {
            String title = URLDecoder.decode(id, "UTF-8");
            List<Song> songs = songService.searchSongs(title);
            if (songs == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(songs, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
