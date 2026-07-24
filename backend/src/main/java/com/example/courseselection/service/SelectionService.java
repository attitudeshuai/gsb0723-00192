package com.example.courseselection.service;

import com.example.courseselection.dto.MakeupExamDTO;
import com.example.courseselection.entity.Selection;
import com.example.courseselection.exception.DuplicateSelectionException;
import com.example.courseselection.repository.SelectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SelectionService {
    @Autowired
    private SelectionRepository selectionRepository;

    public List<Selection> findAll() {
        return selectionRepository.findAll();
    }

    public List<Selection> findByStudentId(Long studentId) {
        return selectionRepository.findByStudentId(studentId);
    }

    public Optional<Selection> findById(Long id) {
        return selectionRepository.findById(id);
    }

    public Selection save(Selection selection) {
        if (selection.getGrade() != null && (selection.getGrade() < 0 || selection.getGrade() > 100)) {
            throw new IllegalArgumentException("成绩必须在 0 到 100 之间");
        }
        if (selection.getStudent() != null && selection.getCourse() != null) {
            Long studentId = selection.getStudent().getId();
            Long courseId = selection.getCourse().getId();
            boolean exists = selection.getId() == null
                    ? selectionRepository.existsByStudentIdAndCourseId(studentId, courseId)
                    : selectionRepository.existsByStudentIdAndCourseIdAndIdNot(studentId, courseId, selection.getId());
            if (exists) {
                throw new DuplicateSelectionException("该学生已选过此课程，不能重复选课");
            }
        }
        return selectionRepository.save(selection);
    }

    public void deleteById(Long id) {
        selectionRepository.deleteById(id);
    }

    public List<MakeupExamDTO> getMakeupExams(String majorName) {
        return selectionRepository.findMakeupExamsByMajor(majorName);
    }
}
