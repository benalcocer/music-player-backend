package com.example.media_backend.song;

import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.search;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Projections.metaSearchScore;
import static com.mongodb.client.model.search.SearchOperator.text;
import static com.mongodb.client.model.search.SearchOptions.searchOptions;
import static com.mongodb.client.model.search.SearchPath.fieldPath;

import com.example.media_backend.model.Song;
import com.example.media_backend.model.Playlist;
import com.example.media_backend.playlist.PlaylistService;
import com.example.media_backend.util.Payload;
import com.mongodb.client.model.search.FuzzySearchOptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class SongService {
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Song> allSongs() {
        return songRepository.findAll();
    }

    public Song saveSong(Payload payload) {
        return saveSong(Song.fromPayload(payload));
    }

    public Song getSong(String id) {
        return songRepository.findById(id).orElse(null);
    }

    public List<Song> getPlaylistSongs(String id) {
        Playlist playlist = playlistService.getPlaylist(id);
        if (playlist != null) {
            return songRepository.findAllById(playlist.getSongIds());
        }
        return null;
    }

    public void deleteSong(String id) {
        songRepository.deleteById(id);
    }

    private Song saveSong(Song newSong) {
        if (newSong == null) {
            return null;
        }
        Song song = songRepository.save(newSong);
        mongoTemplate.update(Song.class)
            .matching(Criteria.where("id").is(song.getId()))
            .apply(new Update().push("song").value(song))
            .first();
        return song;
    }

    public List<Song> searchSongs(String searchText) {
        List<Bson> pipeline = Arrays.asList(
            search(
                text(
                    List.of(fieldPath("artist"), fieldPath("title")), List.of(searchText)
                ).fuzzy(
                    FuzzySearchOptions.fuzzySearchOptions()
                        .maxEdits(2)
                        .prefixLength(0)
                        .maxExpansions(50)
                ),
                searchOptions().index("index01")
            ),
            project(fields(
                include("id"),
                metaSearchScore("score")
            ))
        );

        ArrayList<Document> output = new ArrayList<>();
        mongoTemplate.getDb().getCollection("song").aggregate(pipeline).into(output);
        return songRepository.findAllById(output.stream().map(document -> {
            try {
                return document.getString("_id");
            } catch (Exception e) {
                // ignore
            }
            return null;
        }).filter(Objects::nonNull).toList());
    }
}
