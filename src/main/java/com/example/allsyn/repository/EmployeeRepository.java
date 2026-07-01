package com.example.allsyn.repository;

import com.example.allsyn.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    List<Employee> findByDepartment(String department);
    List<Employee> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);
}