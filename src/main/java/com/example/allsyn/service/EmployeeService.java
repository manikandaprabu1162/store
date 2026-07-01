package com.example.allsyn.service;

import com.example.allsyn.dto.EmployeeRequestDTO;
import com.example.allsyn.dto.EmployeeResponseDTO;

import java.util.List;

public interface EmployeeService {
    EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDTO);
    EmployeeResponseDTO getEmployeeById(Long id);
    EmployeeResponseDTO getEmployeeByEmail(String email);
    List<EmployeeResponseDTO> getAllEmployees();
    List<EmployeeResponseDTO> searchEmployees(String keyword);
    EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO employeeRequestDTO);
    void deleteEmployee(Long id);
    boolean employeeExists(Long id);
    boolean emailExists(String email);
}