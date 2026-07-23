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

    private Selection gradedSelection;
    private Selection ungradedSelection;

    @BeforeEach
    void setUp() {
        Student student = new Student();
        student.setId(1L);
        Course course = new Course();
        course.setId(1L);

        gradedSelection = new Selection();
        gradedSelection.setId(1L);
        gradedSelection.setStudent(student);
        gradedSelection.setCourse(course);
        gradedSelection.setGrade(85.0);

        ungradedSelection = new Selection();
        ungradedSelection.setId(2L);
        ungradedSelection.setStudent(student);
        ungradedSelection.setCourse(course);
        ungradedSelection.setGrade(null);
    }

    @Test
    void delete_courseWithGrade_shouldThrow() {
        when(selectionService.findById(1L)).thenReturn(Optional.of(gradedSelection));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> selectionController.delete(1L));
        assertEquals("已录入成绩不可退选", ex.getMessage());
        verify(selectionService, never()).deleteById(anyLong());
    }

    @Test
    void delete_courseWithoutGrade_shouldSucceed() {
        when(selectionService.findById(2L)).thenReturn(Optional.of(ungradedSelection));

        selectionController.delete(2L);

        verify(selectionService).deleteById(2L);
    }

    @Test
    void delete_notFound_shouldStillAttemptDelete() {
        when(selectionService.findById(99L)).thenReturn(Optional.empty());

        selectionController.delete(99L);

        verify(selectionService).deleteById(99L);
    }
}
