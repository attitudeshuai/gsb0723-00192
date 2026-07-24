package com.example.courseselection.service;

import com.example.courseselection.dto.ClassStatsDTO;
import com.example.courseselection.entity.Student;
import com.example.courseselection.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    public Student save(Student student) {
        if (student.getId() == null && (student.getPassword() == null || student.getPassword().isEmpty())) {
            student.setPassword("123456");
        }
        return studentRepository.save(student);
    }

    public List<Student> saveAll(List<Student> students) {
        for (Student s : students) {
            if (s.getPassword() == null || s.getPassword().isEmpty()) {
                s.setPassword("123456");
            }
        }
        return studentRepository.saveAll(students);
    }

    public Student updateStudent(Long id, Student studentData) {
        Student existing = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
        existing.setStudentNumber(studentData.getStudentNumber());
        existing.setName(studentData.getName());
        existing.setGender(studentData.getGender());
        existing.setBirthDate(studentData.getBirthDate());
        existing.setHometown(studentData.getHometown());
        existing.setClassName(studentData.getClassName());
        existing.setMajor(studentData.getMajor());
        return studentRepository.save(existing);
    }

    public void updatePassword(Long id, String newPassword) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
        student.setPassword(newPassword);
        studentRepository.save(student);
    }

    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }

    public List<ClassStatsDTO> getClassStats(String className) {
        return studentRepository.calculateClassStats(className);
    }
}
