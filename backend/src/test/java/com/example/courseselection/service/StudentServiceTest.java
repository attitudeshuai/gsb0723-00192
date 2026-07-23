package com.example.courseselection.service;

import com.example.courseselection.entity.Student;
import com.example.courseselection.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setStudentNumber("2021001");
        student.setName("TestStudent");
        student.setPassword("123456");
    }

    @Test
    void updatePassword_validNewPassword_shouldUpdateAndSave() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        studentService.updatePassword(1L, "newpass123");

        assertEquals("newpass123", student.getPassword());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void updatePassword_studentNotFound_shouldThrowException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.updatePassword(99L, "newpass123");
        });

        assertEquals("Student not found", exception.getMessage());
        verify(studentRepository, never()).save(any());
    }
}
