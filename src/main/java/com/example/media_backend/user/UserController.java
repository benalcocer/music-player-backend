package com.example.media_backend.user;

import com.example.media_backend.model.User;
import com.example.media_backend.responses.UserResponse;
import com.example.media_backend.util.Payload;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        Optional<User> user = userService.getExistingUser(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@RequestBody Map<String, Object> payload) {
        User user = userService.updateUser(new Payload(payload));
        UserResponse userResponse = user == null ? null : new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getPlaylistIds()
        );
        return new ResponseEntity<>(userResponse, userResponse != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }
}
