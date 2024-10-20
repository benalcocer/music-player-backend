package com.example.media_backend.model;

import com.example.media_backend.util.Payload;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "song")
@Data
@NoArgsConstructor
public class Song {
    @Id
    private String id;
    private String artist;
    private String title;
    private String link;

    public Song(String id, String artist, String title, String link) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public static Song fromPayload(Payload payload) {
        if (payload == null) {
            return null;
        }
        String id = payload.getString("id");
        String artist = payload.getString("artist");
        String title = payload.getString("title");
        String link = payload.getString("link");
        boolean nonNull = Payload.nonNull(
            id,
            artist,
            title,
            link
        );
        return !nonNull ? null : new Song(
            id,
            artist,
            title,
            link
        );
    }
}
