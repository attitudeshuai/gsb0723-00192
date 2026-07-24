package com.example.courseselection.service;

import com.example.courseselection.entity.Student;
import com.example.courseselection.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student(Long id, String name, String password) {
        Student student = new Student();
        student.setId(id);
        student.setStudentNumber("2021001");
        student.setName(name);
        student.setPassword(password);
        return student;
    }

    @Test
    void update_shouldPreserveExistingPassword_whenIncomingPasswordIsNull() {
        Student existing = student(1L, "张三", "storedPass");
        Student incoming = student(1L, "张三改", null);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        studentService.update(1L, incoming);

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(captor.capture());
        assertEquals("storedPass", captor.getValue().getPassword());
        assertEquals("张三改", captor.getValue().getName());
    }

    @Test
    void update_shouldKeepIncomingPassword_whenProvided() {
        Student incoming = student(1L, "张三", "newPass");
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        studentService.update(1L, incoming);

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(captor.capture());
        assertEquals("newPass", captor.getValue().getPassword());
    }

    @Test
    void updatePassword_shouldSetNewPassword() {
        Student existing = student(1L, "张三", "oldPass");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        studentService.updatePassword(1L, "changed");

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(captor.capture());
        assertEquals("changed", captor.getValue().getPassword());
    }
}
