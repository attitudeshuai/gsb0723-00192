package com.example.courseselection.service;

import com.example.courseselection.entity.Course;
import com.example.courseselection.entity.Selection;
import com.example.courseselection.entity.Student;
import com.example.courseselection.repository.SelectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SelectionServiceTest {

    @Mock
    private SelectionRepository selectionRepository;

    @InjectMocks
    private SelectionService selectionService;

    private Selection selection;

    @BeforeEach
    void setUp() {
        Student student = new Student();
        student.setId(1L);
        student.setName("TestStudent");

        Course course = new Course();
        course.setId(1L);
        course.setName("TestCourse");

        selection = new Selection();
        selection.setId(1L);
        selection.setStudent(student);
        selection.setCourse(course);
    }

    @Test
    void save_validGrade_shouldSucceed() {
        selection.setGrade(85.0);
        when(selectionRepository.save(any(Selection.class))).thenReturn(selection);

        Selection result = selectionService.save(selection);

        assertNotNull(result);
        assertEquals(85.0, result.getGrade());
        verify(selectionRepository, times(1)).save(selection);
    }

    @Test
    void save_validGradeZero_shouldSucceed() {
        selection.setGrade(0.0);
        when(selectionRepository.save(any(Selection.class))).thenReturn(selection);

        Selection result = selectionService.save(selection);

        assertNotNull(result);
        assertEquals(0.0, result.getGrade());
        verify(selectionRepository, times(1)).save(selection);
    }

    @Test
    void save_validGradeHundred_shouldSucceed() {
        selection.setGrade(100.0);
        when(selectionRepository.save(any(Selection.class))).thenReturn(selection);

        Selection result = selectionService.save(selection);

        assertNotNull(result);
        assertEquals(100.0, result.getGrade());
        verify(selectionRepository, times(1)).save(selection);
    }

    @Test
    void save_nullGrade_shouldSucceed() {
        selection.setGrade(null);
        when(selectionRepository.save(any(Selection.class))).thenReturn(selection);

        Selection result = selectionService.save(selection);

        assertNotNull(result);
        assertNull(result.getGrade());
        verify(selectionRepository, times(1)).save(selection);
    }

    @Test
    void save_gradeAbove100_shouldThrowException() {
        selection.setGrade(150.0);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            selectionService.save(selection);
        });

        assertEquals("成绩必须在 0 到 100 之间", exception.getMessage());
        verify(selectionRepository, never()).save(any());
    }

    @Test
    void save_gradeBelowZero_shouldThrowException() {
        selection.setGrade(-10.0);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            selectionService.save(selection);
        });

        assertEquals("成绩必须在 0 到 100 之间", exception.getMessage());
        verify(selectionRepository, never()).save(any());
    }
}
