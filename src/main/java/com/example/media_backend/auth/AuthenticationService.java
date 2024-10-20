package com.example.media_backend.auth;

import com.example.media_backend.dtos.LoginUserDto;
import com.example.media_backend.dtos.RegisterUserDto;
import com.example.media_backend.dtos.VerifyUserDto;
import com.example.media_backend.model.User;
import com.example.media_backend.model.User.UserBuilder;
import com.example.media_backend.user.UserRepository;
import com.example.media_backend.util.EmailService;
import jakarta.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    public boolean doesUserExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isUserEnabled(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return false;
        }
        return user.isEnabled();
    }

    public User signup(RegisterUserDto input) {
        Optional<User> user = userRepository.findByEmail(input.getEmail());
        if (user.isPresent()) {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    input.getEmail(),
                    input.getPassword()
                )
            );
            return user.get();
        }
        User newUser = new UserBuilder(
            input.getEmail(),
            passwordEncoder.encode(input.getPassword())
        )
            .setId(UUID.randomUUID().toString())
            .setVerificationCode(generateVerificationCode())
            .setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1))
            .build();
        sendVerificationEmail(newUser);
        return userRepository.save(newUser);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                input.getEmail(),
                input.getPassword()
            )
        );

        return userRepository.findByEmail(input.getEmail()).orElseThrow();
    }

    public void verifyUser(VerifyUserDto input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    private void sendVerificationEmail(User user) { // TODO: Update with company logo
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
            + "<body style=\"font-family: Arial, sans-serif;\">"
            + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
            + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
            + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
            + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
            + "<h3 style=\"color: #333;\">Verification Code:</h3>"
            + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
            + "</div>"
            + "</div>"
            + "</body>"
            + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}