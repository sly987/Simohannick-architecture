package fr.esgi.reseking.controller.validator;

import fr.esgi.reseking.controller.dto.CreateEmployeeDTO;
import fr.esgi.reseking.exception.DuplicateEmailException;
import fr.esgi.reseking.model.Employee;

public class EmployeeValidator {

    private EmployeeValidator() {
    }

    public static void validateEmployeeInput(CreateEmployeeDTO dto) {
        if (dto.getFirstName() == null || dto.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (dto.getLastName() == null || dto.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (dto.getRole() == null || dto.getRole().isBlank()) {
            throw new IllegalArgumentException("Role is required");
        }
    }

    public static void validateEmailNotDuplicate(Employee existingEmployee) {
        if (existingEmployee != null) {
            throw new DuplicateEmailException("An employee with this email already exists");
        }
    }
}
