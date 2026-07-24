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
        student.setStudentNumber("2023001");
        student.setName("张三");
        student.setPassword(null);
    }

    @Test
    void updatePassword_shouldPersistNewPassword() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        studentService.updatePassword(1L, "newSecretPass");

        assertEquals("newSecretPass", student.getPassword());
        verify(studentRepository).save(student);
    }

    @Test
    void updatePassword_shouldOverrideExistingPassword() {
        student.setPassword("oldPassword");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        studentService.updatePassword(1L, "newPassword");

        assertEquals("newPassword", student.getPassword());
        verify(studentRepository).save(student);
    }

    @Test
    void updatePassword_studentNotFound_shouldThrow() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> studentService.updatePassword(99L, "newPass"));
        assertEquals("Student not found", ex.getMessage());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void update_nullPassword_shouldPreserveExistingPassword() {
        student.setPassword("mySecretPass");
        Student incoming = new Student();
        incoming.setId(1L);
        incoming.setStudentNumber("2023001");
        incoming.setName("张三");
        incoming.setPassword(null);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        Student saved = studentService.update(incoming);

        assertEquals("mySecretPass", saved.getPassword());
        verify(studentRepository).save(incoming);
    }

    @Test
    void update_withPassword_shouldUseNewPassword() {
        student.setPassword("oldPassword");
        Student incoming = new Student();
        incoming.setId(1L);
        incoming.setStudentNumber("2023001");
        incoming.setName("张三");
        incoming.setPassword("explicitNewPass");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        Student saved = studentService.update(incoming);

        assertEquals("explicitNewPass", saved.getPassword());
        verify(studentRepository).save(incoming);
    }

    @Test
    void update_notFound_shouldThrow() {
        Student incoming = new Student();
        incoming.setId(99L);

        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> studentService.update(incoming));
        assertEquals("Student not found", ex.getMessage());
        verify(studentRepository, never()).save(any());
    }
}
