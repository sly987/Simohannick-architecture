package fr.esgi.reseking.util;

import fr.esgi.reseking.controller.dto.EmployeeDTO;
import fr.esgi.reseking.model.Employee;
import fr.esgi.reseking.model.enums.Role;

public class EmployeeMapper {

    private EmployeeMapper() {
    }

    public static Employee toEntity(EmployeeDTO dto) {
        Role role = Role.valueOf(dto.getRole().toUpperCase());

        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setRole(role);

        return employee;
    }

    public static EmployeeDTO toDto(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setRole(employee.getRole().name());
        return dto;
    }
}

