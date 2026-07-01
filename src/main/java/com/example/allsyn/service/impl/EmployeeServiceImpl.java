package com.example.allsyn.service.impl;

import com.example.allsyn.dto.EmployeeRequestDTO;
import com.example.allsyn.dto.EmployeeResponseDTO;
import com.example.allsyn.model.Employee;
import com.example.allsyn.repository.EmployeeRepository;
import com.example.allsyn.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO request) {
        log.info("Creating new employee with email: {}", request.getEmail());

        if (emailExists(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .department(request.getDepartment())
                .salary(request.getSalary())
                .hireDate(request.getHireDate())
                .phone(request.getPhone())
                .address(request.getAddress())
                .position(request.getPosition())
                .build();

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created with ID: {}", savedEmployee.getId());

        return convertToResponseDTO(savedEmployee);
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {
        log.info("Fetching employee by ID: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + id));
        return convertToResponseDTO(employee);
    }

    @Override
    public EmployeeResponseDTO getEmployeeByEmail(String email) {
        log.info("Fetching employee by email: {}", email);
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with email: " + email));
        return convertToResponseDTO(employee);
    }

    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponseDTO> searchEmployees(String keyword) {
        log.info("Searching employees with keyword: {}", keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllEmployees();
        }
        return employeeRepository.findByFirstNameContainingOrLastNameContaining(keyword, keyword)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO request) {
        log.info("Updating employee with ID: {}", id);

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + id));

        if (!existingEmployee.getEmail().equals(request.getEmail()) &&
                employeeRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        existingEmployee.setFirstName(request.getFirstName());
        existingEmployee.setLastName(request.getLastName());
        existingEmployee.setEmail(request.getEmail());
        existingEmployee.setDepartment(request.getDepartment());
        existingEmployee.setSalary(request.getSalary());
        existingEmployee.setHireDate(request.getHireDate());
        existingEmployee.setPhone(request.getPhone());
        existingEmployee.setAddress(request.getAddress());
        existingEmployee.setPosition(request.getPosition());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        log.info("Employee updated with ID: {}", updatedEmployee.getId());

        return convertToResponseDTO(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with ID: {}", id);
        if (!employeeExists(id)) {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }
        employeeRepository.deleteById(id);
        log.info("Employee deleted with ID: {}", id);
    }

    @Override
    public boolean employeeExists(Long id) {
        return employeeRepository.existsById(id);
    }

    @Override
    public boolean emailExists(String email) {
        return employeeRepository.existsByEmail(email);
    }

    private EmployeeResponseDTO convertToResponseDTO(Employee employee) {
        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .fullName(employee.getFirstName() + " " + employee.getLastName())
                .email(employee.getEmail())
                .department(employee.getDepartment())
                .salary(employee.getSalary())
                .hireDate(employee.getHireDate())
                .phone(employee.getPhone())
                .address(employee.getAddress())
                .position(employee.getPosition())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}