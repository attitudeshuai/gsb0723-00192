package com.example.courseselection.service;

import com.example.courseselection.entity.Major;
import com.example.courseselection.entity.Student;
import com.example.courseselection.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
        student.setGender("男");
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

    @Test
    void save_newStudentWithoutPassword_shouldSetDefaultPassword() {
        Student newStudent = new Student();
        newStudent.setStudentNumber("2021002");
        newStudent.setName("NewStudent");
        newStudent.setGender("女");
        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        Student result = studentService.save(newStudent);

        assertEquals("123456", result.getPassword());
        verify(studentRepository, times(1)).save(newStudent);
    }

    @Test
    void save_newStudentWithPassword_shouldKeepPassword() {
        Student newStudent = new Student();
        newStudent.setStudentNumber("2021003");
        newStudent.setName("AnotherStudent");
        newStudent.setGender("男");
        newStudent.setPassword("custompass");
        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        Student result = studentService.save(newStudent);

        assertEquals("custompass", result.getPassword());
        verify(studentRepository, times(1)).save(newStudent);
    }

    @Test
    void updateStudent_shouldNotOverwritePassword() {
        Student existingStudent = new Student();
        existingStudent.setId(1L);
        existingStudent.setStudentNumber("2021001");
        existingStudent.setName("TestStudent");
        existingStudent.setPassword("secretpass");
        existingStudent.setGender("男");

        Student updateData = new Student();
        updateData.setStudentNumber("2021001");
        updateData.setName("UpdatedName");
        updateData.setGender("女");
        updateData.setHometown("上海");
        updateData.setClassName("计科2班");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(existingStudent);

        Student result = studentService.updateStudent(1L, updateData);

        assertEquals("secretpass", result.getPassword());
        assertEquals("UpdatedName", result.getName());
        assertEquals("女", result.getGender());
        assertEquals("上海", result.getHometown());
        verify(studentRepository, times(1)).save(existingStudent);
    }

    @Test
    void updateStudent_studentNotFound_shouldThrowException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        Student updateData = new Student();
        updateData.setName("Test");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.updateStudent(99L, updateData);
        });

        assertEquals("Student not found", exception.getMessage());
    }
}
