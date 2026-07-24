package com.example.courseselection.controller;

import com.example.courseselection.entity.Selection;
import com.example.courseselection.service.SelectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SelectionControllerTest {

    @Mock
    private SelectionService selectionService;

    @InjectMocks
    private SelectionController selectionController;

    private Selection selectionWithGrade(Long id, Double grade) {
        Selection selection = new Selection();
        selection.setId(id);
        selection.setGrade(grade);
        return selection;
    }

    @Test
    void delete_shouldSucceed_whenGradeNotEntered() {
        Selection selection = selectionWithGrade(1L, null);
        when(selectionService.findById(1L)).thenReturn(Optional.of(selection));

        assertDoesNotThrow(() -> selectionController.delete(1L));
        verify(selectionService).deleteById(1L);
    }

    @Test
    void delete_shouldReject_whenGradeAlreadyEntered() {
        Selection selection = selectionWithGrade(2L, 88.0);
        when(selectionService.findById(2L)).thenReturn(Optional.of(selection));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> selectionController.delete(2L));
        assertEquals("已录入成绩不可退选", ex.getMessage());
        verify(selectionService, never()).deleteById(2L);
    }
}
