package com.example.courseselection.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", resolveDataIntegrityMessage(ex));
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "操作失败：" + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String resolveDataIntegrityMessage(DataIntegrityViolationException ex) {
        Throwable cause = ex.getMostSpecificCause();
        String msg = cause != null ? cause.getMessage().toLowerCase() : ex.getMessage().toLowerCase();

        if (msg.contains("unique_selection") || (msg.contains("duplicate") && msg.contains("selection"))) {
            return "该学生已选过此课程，不可重复选择";
        }
        if (msg.contains("student_number") || (msg.contains("duplicate") && msg.contains("student"))) {
            return "该学号已存在，不可重复添加";
        }
        if (msg.contains("course_number") || (msg.contains("duplicate") && msg.contains("course"))) {
            return "该课程号已存在，不可重复添加";
        }
        if (msg.contains("foreign key") || msg.contains("constraint")) {
            if (msg.contains("selection") && msg.contains("student")) {
                return "无法删除：该学生存在选课记录";
            }
            if (msg.contains("selection") && msg.contains("course")) {
                return "无法删除：该课程存在选课记录";
            }
        }
        return "数据冲突：该记录已存在或存在关联数据";
    }
}
