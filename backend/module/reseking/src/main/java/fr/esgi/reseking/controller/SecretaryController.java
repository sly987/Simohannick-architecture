package fr.esgi.reseking.controller;

import fr.esgi.reseking.controller.dto.EmployeeDTO;
import fr.esgi.reseking.controller.response.CreationApiResponse;
import fr.esgi.reseking.controller.validator.EmployeeValidator;
import fr.esgi.reseking.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/secretary")
public class SecretaryController {

    private final EmployeeService employeeService;

    public SecretaryController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/employees")
    public ResponseEntity<CreationApiResponse> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeValidator.validateEmployeeInput(employeeDTO);
        Integer employeeId = employeeService.addEmployee(employeeDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreationApiResponse("Employee created successfully", employeeId));
    }

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
}
