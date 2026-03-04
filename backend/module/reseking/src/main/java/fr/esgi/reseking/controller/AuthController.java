package fr.esgi.reseking.controller;

import fr.esgi.reseking.controller.dto.LoginDTO;
import fr.esgi.reseking.controller.response.LoginApiResponse;
import fr.esgi.reseking.controller.validator.LoginValidator;
import fr.esgi.reseking.model.Employee;
import fr.esgi.reseking.service.AuthService;
import fr.esgi.reseking.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication endpoints for login")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user with email and password and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<LoginApiResponse> login(@RequestBody LoginDTO loginDTO) {
        LoginValidator.validateLoginInput(loginDTO);
        Employee employee = authService.authenticate(loginDTO);
        String token = jwtService.generateToken(employee);
        return ResponseEntity.ok(new LoginApiResponse(token, employee.getRole().name()));
    }
}

