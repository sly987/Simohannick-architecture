package fr.esgi.reseking.controller.validator;

import fr.esgi.reseking.controller.dto.LoginDTO;

public class LoginValidator {

    private LoginValidator() {
    }

    public static void validateLoginInput(LoginDTO dto) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
    }
}

