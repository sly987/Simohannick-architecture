package fr.esgi.reseking.service;

import fr.esgi.reseking.controller.dto.LoginDTO;
import fr.esgi.reseking.exception.InvalidCredentialsException;
import fr.esgi.reseking.model.Employee;
import fr.esgi.reseking.repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Employee authenticate(LoginDTO loginDTO) {
        Employee employee = employeeRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), employee.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return employee;
    }
}

