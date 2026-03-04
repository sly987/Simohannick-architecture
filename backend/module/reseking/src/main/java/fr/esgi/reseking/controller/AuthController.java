package fr.esgi.reseking.controller;

import fr.esgi.reseking.controller.dto.LoginDTO;
import fr.esgi.reseking.controller.response.LoginApiResponse;
import fr.esgi.reseking.controller.validator.LoginValidator;
import fr.esgi.reseking.model.Employee;
import fr.esgi.reseking.service.AuthService;
import fr.esgi.reseking.service.EmployeeService;
import fr.esgi.reseking.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final EmployeeService employeeService;

    public AuthController(AuthService authService, JwtService jwtService, EmployeeService employeeService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.employeeService = employeeService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginApiResponse> login(@RequestBody LoginDTO loginDTO) {
        LoginValidator.validateLoginInput(loginDTO);
        Employee employee = authService.authenticate(loginDTO);
        String token = jwtService.generateToken(employee);
        return ResponseEntity.ok(new LoginApiResponse(token, employee.getRole().name()));
    }

    @GetMapping("/me")
    public ResponseEntity<EmployeeDTO> getMe(Authentication authentication) {
        String email = authentication.getName();
        EmployeeDTO employee = employeeService.getCurrentUser(email);
        return ResponseEntity.ok(employee);
    }
}

