package fr.esgi.reseking.mapper;

import fr.esgi.reseking.controller.dto.CreateEmployeeDTO;
import fr.esgi.reseking.controller.dto.EmployeeDTO;
import fr.esgi.reseking.model.Employee;
import fr.esgi.reseking.model.enums.Role;

import java.util.Collections;
import java.util.stream.Collectors;

public class EmployeeMapper {

    private EmployeeMapper() {
    }

    public static Employee toEntity(CreateEmployeeDTO dto) {
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
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setRole(employee.getRole().name());

        if (employee.getReservations() != null) {
            dto.setReservations(employee.getReservations()
                    .stream()
                    .map(ReservationMapper::mapToDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setReservations(Collections.emptyList());
        }

        return dto;
    }
}

