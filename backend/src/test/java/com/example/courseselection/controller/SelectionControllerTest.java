package com.example.courseselection.controller;

import com.example.courseselection.entity.Course;
import com.example.courseselection.entity.Selection;
import com.example.courseselection.entity.Student;
import com.example.courseselection.service.SelectionService;
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
class SelectionControllerTest {

    @Mock
    private SelectionService selectionService;

    @InjectMocks
    private SelectionController selectionController;

    private Selection selectionWithGrade;
    private Selection selectionWithoutGrade;

    @BeforeEach
    void setUp() {
        Student student = new Student();
        student.setId(1L);
        student.setName("TestStudent");

        Course course = new Course();
        course.setId(1L);
        course.setName("TestCourse");

        selectionWithGrade = new Selection();
        selectionWithGrade.setId(1L);
        selectionWithGrade.setStudent(student);
        selectionWithGrade.setCourse(course);
        selectionWithGrade.setGrade(85.0);

        selectionWithoutGrade = new Selection();
        selectionWithoutGrade.setId(2L);
        selectionWithoutGrade.setStudent(student);
        selectionWithoutGrade.setCourse(course);
        selectionWithoutGrade.setGrade(null);
    }

    @Test
    void delete_selectionWithGrade_shouldThrowException() {
        when(selectionService.findById(1L)).thenReturn(Optional.of(selectionWithGrade));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            selectionController.delete(1L);
        });

        assertEquals("已录入成绩不可退选", exception.getMessage());
        verify(selectionService, never()).deleteById(any());
    }

    @Test
    void delete_selectionWithoutGrade_shouldSucceed() {
        when(selectionService.findById(2L)).thenReturn(Optional.of(selectionWithoutGrade));

        selectionController.delete(2L);

        verify(selectionService, times(1)).deleteById(2L);
    }

    @Test
    void delete_selectionNotFound_shouldSucceed() {
        when(selectionService.findById(99L)).thenReturn(Optional.empty());

        selectionController.delete(99L);

        verify(selectionService, times(1)).deleteById(99L);
    }
}
