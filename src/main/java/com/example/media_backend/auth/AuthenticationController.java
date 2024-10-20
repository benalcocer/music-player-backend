package com.example.media_backend.auth;

import com.example.media_backend.dtos.LoginUserDto;
import com.example.media_backend.dtos.RegisterUserDto;
import com.example.media_backend.dtos.VerifyUserDto;
import com.example.media_backend.exceptions.MissingBodyPropertiesException;
import com.example.media_backend.model.User;
import com.example.media_backend.responses.LoginResponse;
import com.example.media_backend.responses.UserResponse;
import com.example.media_backend.util.Payload;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/exists")
    public ResponseEntity<String> doesUserExist(@RequestBody Map<String, Object> map) {
        Payload payload = new Payload(map);
        String email = payload.getString("email");
        if (email != null && authenticationService.doesUserExist(email)) {
            return new ResponseEntity<>("User exists.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User does not exist.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/enabled")
    public ResponseEntity<String> isUserEnabled(@RequestBody Map<String, Object> map) {
        Payload payload = new Payload(map);
        String email = payload.getString("email");
        if (email != null && authenticationService.isUserEnabled(email)) {
            return new ResponseEntity<>("User is enabled.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User is not enabled.", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse(
            jwtToken,
            jwtService.getExpirationTime(),
            new UserResponse(
                authenticatedUser.getId(),
                authenticatedUser.getEmail(),
                authenticatedUser.getPlaylistIds()
            )
        );

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody VerifyUserDto verifyUserDto) throws MissingBodyPropertiesException {
        VerifyUserDto.checkParameters(verifyUserDto);
        authenticationService.verifyUser(verifyUserDto);
        return ResponseEntity.ok("Account verified successfully");
    }

    @PostMapping("/resend")
    public ResponseEntity<String> resendVerificationCode(@RequestBody Map<String, Object> map) {
        Payload payload = new Payload(map);
        String email = payload.getStringOrDefault("email", "");
        authenticationService.resendVerificationCode(email);
        return ResponseEntity.ok("Verification code sent");
    }
}