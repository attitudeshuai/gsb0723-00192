package com.example.courseselection.controller;

import com.example.courseselection.dto.LoginRequest;
import com.example.courseselection.entity.Admin;
import com.example.courseselection.entity.Student;
import com.example.courseselection.repository.AdminRepository;
import com.example.courseselection.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private AuthController authController;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setStudentNumber("2023001");
        student.setName("张三");
    }

    @Test
    void login_studentWithChangedPassword_newPasswordWorks() {
        student.setPassword("myNewPass");
        LoginRequest req = new LoginRequest();
        req.setUsername("2023001");
        req.setPassword("myNewPass");

        when(adminRepository.findByUsername("2023001")).thenReturn(Optional.empty());
        when(studentRepository.findByStudentNumber("2023001")).thenReturn(Optional.of(student));

        ResponseEntity<?> resp = authController.login(req);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) resp.getBody();
        assertEquals("student", body.get("role"));
    }

    @Test
    void login_studentWithChangedPassword_oldDefault123456Fails() {
        student.setPassword("myNewPass");
        LoginRequest req = new LoginRequest();
        req.setUsername("2023001");
        req.setPassword("123456");

        when(adminRepository.findByUsername("2023001")).thenReturn(Optional.empty());
        when(studentRepository.findByStudentNumber("2023001")).thenReturn(Optional.of(student));

        ResponseEntity<?> resp = authController.login(req);

        assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
    }

    @Test
    void login_studentWithNullPassword_123456Fails() {
        student.setPassword(null);
        LoginRequest req = new LoginRequest();
        req.setUsername("2023001");
        req.setPassword("123456");

        when(adminRepository.findByUsername("2023001")).thenReturn(Optional.empty());
        when(studentRepository.findByStudentNumber("2023001")).thenReturn(Optional.of(student));

        ResponseEntity<?> resp = authController.login(req);

        assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
    }

    @Test
    void login_studentWrongPassword_fails() {
        student.setPassword("secret");
        LoginRequest req = new LoginRequest();
        req.setUsername("2023001");
        req.setPassword("wrong");

        when(adminRepository.findByUsername("2023001")).thenReturn(Optional.empty());
        when(studentRepository.findByStudentNumber("2023001")).thenReturn(Optional.of(student));

        ResponseEntity<?> resp = authController.login(req);

        assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
    }

    @Test
    void login_adminCorrectPassword_succeeds() {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setPassword("123456");

        LoginRequest req = new LoginRequest();
        req.setUsername("admin");
        req.setPassword("123456");

        when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(admin));

        ResponseEntity<?> resp = authController.login(req);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) resp.getBody();
        assertEquals("admin", body.get("role"));
    }
}
