package com.example.media_backend.user;

import com.example.media_backend.model.User;
import com.example.media_backend.util.Payload;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getExistingUser(String id) {
        return id != null ? userRepository.findById(id) : Optional.empty();
    }

    public User updateUser(Payload payload) {
        String id = payload.getString("id");
        String email = payload.getString("email");
        List<String> playlistIds = payload.getStringList("playlistIds");
        if (!Payload.nonNull(id, email, playlistIds)) {
            System.out.println("A property was null");
            return null;
        }
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setId(id);
            user.setEmail(email);
            user.setPlaylistIds(playlistIds);
            return updateUser(user);
        }
        return null;
    }

    public User updateUser(User newUser) {
        if (newUser == null) {
            return null;
        }
        User user = userRepository.save(newUser);
        mongoTemplate.update(User.class)
            .matching(Criteria.where("id").is(user.getId()))
            .apply(new Update().push("user").value(user))
            .first();
        return user;
    }
}
