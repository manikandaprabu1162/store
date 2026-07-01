package com.example.allsyn.repository;

import com.example.allsyn.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Custom query method to check if email exists
    boolean existsByEmail(String email);

    // Find student by email
    Optional<Student> findByEmail(String email);

    // Custom query to check if email exists excluding specific student
    boolean existsByEmailAndIdNot(String email, Long id);
}
