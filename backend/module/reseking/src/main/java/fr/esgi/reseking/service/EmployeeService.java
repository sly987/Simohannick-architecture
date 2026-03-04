package fr.esgi.reseking.service;

import fr.esgi.reseking.controller.dto.EmployeeDTO;
import fr.esgi.reseking.controller.validator.EmployeeValidator;
import fr.esgi.reseking.exception.DataNotFoundException;
import fr.esgi.reseking.model.Employee;
import fr.esgi.reseking.repository.EmployeeRepository;
import fr.esgi.reseking.util.EmployeeMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Integer addEmployee(EmployeeDTO employeeDTO) {
        Employee existing = employeeRepository.findByEmail(employeeDTO.getEmail()).orElse(null);
        EmployeeValidator.validateEmailNotDuplicate(existing);

        Employee employee = EmployeeMapper.toEntity(employeeDTO);
        employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        Employee savedEmployee = employeeRepository.save(employee);
        return savedEmployee.getId();
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(EmployeeMapper::toDto)
                .toList();
    }

    public EmployeeDTO getCurrentUser(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Employee not found with email: " + email));
        return EmployeeMapper.toDto(employee);
    }
}
