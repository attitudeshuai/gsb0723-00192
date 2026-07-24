package com.example.courseselection.exception;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handle_shouldReturnDuplicateSelectionMessage_whenUniqueConstraintOnStudentAndCourse() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException(
                "could not execute statement",
                new RuntimeException("Duplicate entry '10-3' for key 'UK_student_id_course_id'"));

        ResponseEntity<Map<String, String>> response = handler.handleDataIntegrityViolationException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("该学生已选择此课程，请勿重复添加", response.getBody().get("message"));
    }

    @Test
    void handle_shouldReturnGenericMessage_whenConstraintUnrelatedToSelection() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException(
                "could not execute statement",
                new RuntimeException("Duplicate entry 'zhangsan' for key 'student.student_number'"));

        ResponseEntity<Map<String, String>> response = handler.handleDataIntegrityViolationException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("操作失败：数据已存在或违反数据完整性约束", response.getBody().get("message"));
    }
}
