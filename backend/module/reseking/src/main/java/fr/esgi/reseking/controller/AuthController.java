package fr.esgi.reseking.controller;

import fr.esgi.reseking.controller.dto.LoginDTO;
import fr.esgi.reseking.controller.response.LoginApiResponse;
import fr.esgi.reseking.controller.validator.LoginValidator;
import fr.esgi.reseking.model.Employee;
import fr.esgi.reseking.service.AuthService;
import fr.esgi.reseking.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginApiResponse> login(@RequestBody LoginDTO loginDTO) {
        LoginValidator.validateLoginInput(loginDTO);
        Employee employee = authService.authenticate(loginDTO);
        String token = jwtService.generateToken(employee);
        return ResponseEntity.ok(new LoginApiResponse(token, employee.getRole().name()));
    }
}

