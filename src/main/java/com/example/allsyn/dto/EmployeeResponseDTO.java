package com.example.allsyn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String department;
    private Double salary;
    private LocalDate hireDate;
    private String phone;
    private String address;
    private String position;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}