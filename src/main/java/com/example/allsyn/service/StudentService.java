package com.example.allsyn.service;

import com.example.allsyn.dto.StudentDTO;

import java.util.List;

public interface StudentService {

    StudentDTO createStudent(StudentDTO studentDTO);

    StudentDTO getStudentById(Long id);

    StudentDTO getStudentByEmail(String email);

    List<StudentDTO> getAllStudents();

    StudentDTO updateStudent(Long id, StudentDTO studentDTO);

    void deleteStudent(Long id);

    boolean studentExists(Long id);

    boolean emailExists(String email);
}