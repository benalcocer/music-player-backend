package com.example.media_backend.model;

import com.example.media_backend.util.Payload;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "playlist")
@Data
@NoArgsConstructor
public class Playlist {

    @Id
    private String id;
    private String title;
    private List<String> songIds;

    public Playlist(String id, String title, List<String> songIds) {
        this.id = id;
        this.title = title;
        this.songIds = songIds;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getSongIds() {
        return songIds;
    }

    public static Playlist fromPayload(Payload payload) {
        if (payload == null) {
            return null;
        }
        String id = payload.getString("id");
        String title = payload.getString("title");
        List<String> songIds = payload.getStringListOrDefault("songIds", List.of());
        boolean nonNull = Payload.nonNull(
            id,
            title,
            songIds
        );
        return !nonNull ? null : new Playlist(
            id,
            title,
            songIds
        );
    }
}
