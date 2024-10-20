package com.example.media_backend.dtos;

import com.example.media_backend.exceptions.MissingBodyPropertiesException;

public class VerifyUserDto {
    private String email;
    private String verificationCode;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public static void checkParameters(VerifyUserDto verifyUserDto) throws MissingBodyPropertiesException {
        if (verifyUserDto.getEmail() == null) {
            throw new MissingBodyPropertiesException("Missing email property.");
        }
        if (verifyUserDto.getVerificationCode() == null) {
            throw new MissingBodyPropertiesException("Missing verificationCode property.");
        }
    }
}