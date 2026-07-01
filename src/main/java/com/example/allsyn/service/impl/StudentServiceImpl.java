package com.example.allsyn.service.impl;

import com.example.allsyn.dto.StudentDTO;
import com.example.allsyn.model.Student;
import com.example.allsyn.model.Student;
import com.example.allsyn.repository.StudentRepository;
import com.example.allsyn.service.StudentService;
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
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        log.info("Creating new student with email: {}", studentDTO.getEmail());

        // Check if email already exists
        if (emailExists(studentDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + studentDTO.getEmail());
        }

        // Convert DTO to Entity
        Student student = new Student();
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());

        // Save to database
        Student savedStudent = studentRepository.save(student);
        log.info("Student created with ID: {}", savedStudent.getId());

        return convertToDTO(savedStudent);
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        log.info("Fetching student by ID: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + id));
        return convertToDTO(student);
    }

    @Override
    public StudentDTO getStudentByEmail(String email) {
        log.info("Fetching student by email: {}", email);
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with email: " + email));
        return convertToDTO(student);
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        log.info("Fetching all students");
        return studentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        log.info("Updating student with ID: {}", id);

        // Find existing student
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + id));

        // Check if email is being changed and if new email already exists
        if (!existingStudent.getEmail().equals(studentDTO.getEmail()) &&
                studentRepository.existsByEmailAndIdNot(studentDTO.getEmail(), id)) {
            throw new IllegalArgumentException("Email already exists: " + studentDTO.getEmail());
        }

        // Update fields
        existingStudent.setName(studentDTO.getName());
        existingStudent.setEmail(studentDTO.getEmail());

        // Save to database
        Student updatedStudent = studentRepository.save(existingStudent);
        log.info("Student updated with ID: {}", updatedStudent.getId());

        return convertToDTO(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        log.info("Deleting student with ID: {}", id);
        if (!studentExists(id)) {
            throw new IllegalArgumentException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
        log.info("Student deleted with ID: {}", id);
    }

    @Override
    public boolean studentExists(Long id) {
        return studentRepository.existsById(id);
    }

    @Override
    public boolean emailExists(String email) {
        return studentRepository.existsByEmail(email);
    }

    // Helper method to convert Entity to DTO
    private StudentDTO convertToDTO(Student student) {
        return new StudentDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }
}