package com.example.courseselection.controller;

import com.example.courseselection.dto.LoginRequest;
import com.example.courseselection.entity.Student;
import com.example.courseselection.repository.AdminRepository;
import com.example.courseselection.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest(String username, String password) {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);
        return request;
    }

    private Student student(Long id, String number, String password) {
        Student student = new Student();
        student.setId(id);
        student.setStudentNumber(number);
        student.setName("张三");
        student.setPassword(password);
        return student;
    }

    @Test
    void login_shouldSucceed_whenStudentUsesChangedPassword() {
        Student student = student(10L, "2021001", "newPass");
        when(adminRepository.findByUsername("2021001")).thenReturn(Optional.empty());
        when(studentRepository.findByStudentNumber("2021001")).thenReturn(Optional.of(student));

        ResponseEntity<?> response = authController.login(loginRequest("2021001", "newPass"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void login_shouldFail_whenStudentPasswordChangedButDefaultUsed() {
        Student student = student(10L, "2021001", "newPass");
        when(adminRepository.findByUsername("2021001")).thenReturn(Optional.empty());
        when(studentRepository.findByStudentNumber("2021001")).thenReturn(Optional.of(student));

        ResponseEntity<?> response = authController.login(loginRequest("2021001", "123456"));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void login_shouldFail_whenStudentPasswordNull() {
        Student student = student(11L, "2021002", null);
        when(adminRepository.findByUsername("2021002")).thenReturn(Optional.empty());
        when(studentRepository.findByStudentNumber("2021002")).thenReturn(Optional.of(student));

        ResponseEntity<?> response = authController.login(loginRequest("2021002", "123456"));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
