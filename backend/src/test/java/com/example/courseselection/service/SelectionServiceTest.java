package com.example.courseselection.service;

import com.example.courseselection.entity.Selection;
import com.example.courseselection.repository.SelectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SelectionServiceTest {

    @Mock
    private SelectionRepository selectionRepository;

    @InjectMocks
    private SelectionService selectionService;

    private Selection selectionWithGrade(Double grade) {
        Selection selection = new Selection();
        selection.setGrade(grade);
        return selection;
    }

    @Test
    void save_shouldPersist_whenGradeWithinRange() {
        Selection selection = selectionWithGrade(85.0);
        when(selectionRepository.save(any(Selection.class))).thenReturn(selection);

        assertDoesNotThrow(() -> selectionService.save(selection));
        verify(selectionRepository).save(selection);
    }

    @Test
    void save_shouldPersist_whenGradeIsNull() {
        Selection selection = selectionWithGrade(null);
        when(selectionRepository.save(any(Selection.class))).thenReturn(selection);

        assertDoesNotThrow(() -> selectionService.save(selection));
        verify(selectionRepository).save(selection);
    }

    @Test
    void save_shouldReject_whenGradeAboveRange() {
        Selection selection = selectionWithGrade(150.0);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> selectionService.save(selection));
        assertEquals("成绩必须在 0 到 100 之间", ex.getMessage());
        verify(selectionRepository, never()).save(any(Selection.class));
    }

    @Test
    void save_shouldReject_whenGradeBelowRange() {
        Selection selection = selectionWithGrade(-10.0);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> selectionService.save(selection));
        assertEquals("成绩必须在 0 到 100 之间", ex.getMessage());
        verify(selectionRepository, never()).save(any(Selection.class));
    }
}
