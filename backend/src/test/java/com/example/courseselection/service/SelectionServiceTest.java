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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SelectionServiceTest {

    @Mock
    private SelectionRepository selectionRepository;

    @InjectMocks
    private SelectionService selectionService;

    private Selection newSelection;

    @BeforeEach
    void setUp() {
        Student student = new Student();
        student.setId(1L);
        Course course = new Course();
        course.setId(1L);
        newSelection = new Selection();
        newSelection.setStudent(student);
        newSelection.setCourse(course);
    }

    @Test
    void save_validGrade85_shouldSucceed() {
        newSelection.setGrade(85.0);
        when(selectionRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(false);
        when(selectionRepository.save(any(Selection.class))).thenAnswer(inv -> inv.getArgument(0));

        Selection saved = selectionService.save(newSelection);

        assertEquals(85.0, saved.getGrade());
        verify(selectionRepository).save(newSelection);
    }

    @Test
    void save_validGrade0_shouldSucceed() {
        newSelection.setGrade(0.0);
        when(selectionRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(false);
        when(selectionRepository.save(any(Selection.class))).thenAnswer(inv -> inv.getArgument(0));

        Selection saved = selectionService.save(newSelection);

        assertEquals(0.0, saved.getGrade());
        verify(selectionRepository).save(newSelection);
    }

    @Test
    void save_validGrade100_shouldSucceed() {
        newSelection.setGrade(100.0);
        when(selectionRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(false);
        when(selectionRepository.save(any(Selection.class))).thenAnswer(inv -> inv.getArgument(0));

        Selection saved = selectionService.save(newSelection);

        assertEquals(100.0, saved.getGrade());
        verify(selectionRepository).save(newSelection);
    }

    @Test
    void save_grade150_shouldThrow() {
        newSelection.setGrade(150.0);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> selectionService.save(newSelection));
        assertEquals("成绩必须在 0 到 100 之间", ex.getMessage());
        verify(selectionRepository, never()).save(any());
    }

    @Test
    void save_gradeNegative10_shouldThrow() {
        newSelection.setGrade(-10.0);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> selectionService.save(newSelection));
        assertEquals("成绩必须在 0 到 100 之间", ex.getMessage());
        verify(selectionRepository, never()).save(any());
    }

    @Test
    void save_nullGrade_shouldSucceed() {
        newSelection.setGrade(null);
        when(selectionRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(false);
        when(selectionRepository.save(any(Selection.class))).thenAnswer(inv -> inv.getArgument(0));

        Selection saved = selectionService.save(newSelection);

        assertNull(saved.getGrade());
        verify(selectionRepository).save(newSelection);
    }

    @Test
    void save_duplicateSelection_shouldThrow() {
        newSelection.setGrade(null);
        when(selectionRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> selectionService.save(newSelection));
        assertEquals("该学生已选过此课程，不能重复选课", ex.getMessage());
        verify(selectionRepository, never()).save(any());
    }

    @Test
    void save_updateExistingSameStudentCourse_shouldSucceed() {
        newSelection.setId(10L);
        newSelection.setGrade(90.0);
        when(selectionRepository.existsByStudentIdAndCourseIdAndIdNot(1L, 1L, 10L)).thenReturn(false);
        when(selectionRepository.save(any(Selection.class))).thenAnswer(inv -> inv.getArgument(0));

        Selection saved = selectionService.save(newSelection);

        assertEquals(90.0, saved.getGrade());
        verify(selectionRepository).save(newSelection);
    }
}
