package com.example.media_backend.responses;

import java.util.List;

public class UserResponse {

    private String id;
    private String email;

    private List<String> playlistIds;

    public UserResponse(String id, String email, List<String> playlistIds) {
        this.id = id;
        this.email = email;
        this.playlistIds = playlistIds;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getPlaylistIds() {
        return playlistIds;
    }
}
