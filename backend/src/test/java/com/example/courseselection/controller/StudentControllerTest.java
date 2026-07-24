package com.example.courseselection.controller;

import com.example.courseselection.dto.PasswordUpdateRequest;
import com.example.courseselection.entity.Student;
import com.example.courseselection.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void updatePassword_shouldCallServiceWithNewPassword() {
        PasswordUpdateRequest request = new PasswordUpdateRequest();
        request.setNewPassword("myNewPass");

        studentController.updatePassword(1L, request);

        verify(studentService).updatePassword(1L, "myNewPass");
    }

    @Test
    void update_shouldCallServiceUpdate() {
        Student student = new Student();
        student.setStudentNumber("2023001");
        student.setName("张三");

        studentController.update(1L, student);

        verify(studentService).update(student);
    }
}
