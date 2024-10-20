package com.example.media_backend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Document(collection = "user")
@Data
public class User implements UserDetails {

    @Id
    private String id;

    private String email;   // Required
    private String password;    // Required

    private String verificationCode;
    private LocalDateTime verificationCodeExpiresAt;
    private List<String> playlistIds;
    private boolean enabled;

    public User() {
        playlistIds = new ArrayList<>();
        enabled = false;
    }

    public User(
        String id,
        String email,
        String password,
        String verificationCode,
        List<String> playlistIds
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.verificationCode = verificationCode;
        this.playlistIds = playlistIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPlaylistIds(List<String> playlistIds) {
        this.playlistIds = playlistIds;
    }

    public String getEmail() {
        return email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public LocalDateTime getVerificationCodeExpiresAt() {
        return verificationCodeExpiresAt;
    }

    public List<String> getPlaylistIds() {
        return playlistIds;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public void setVerificationCodeExpiresAt(LocalDateTime verificationCodeExpiresAt) {
        this.verificationCodeExpiresAt = verificationCodeExpiresAt;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public static class UserBuilder {
        private final User user = new User();
        public UserBuilder(String email, String password) {
            user.email = email;
            user.password = password;
        }

        public UserBuilder setId(String id) {
            user.id = id;
            return this;
        }

        public UserBuilder setVerificationCode(String verificationCode) {
            user.verificationCode = verificationCode;
            return this;
        }

        public UserBuilder setVerificationCodeExpiresAt(LocalDateTime verificationCodeExpiresAt) {
            user.verificationCodeExpiresAt = verificationCodeExpiresAt;
            return this;
        }

        public UserBuilder setPlaylistIds(List<String> playlistIds) {
            user.playlistIds = playlistIds;
            return this;
        }

        public User build() {
            return user;
        }
    }
}
