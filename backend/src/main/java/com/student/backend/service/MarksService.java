package com.student.backend.service;

import com.student.backend.model.Marks;
import com.student.backend.model.Student;
import com.student.backend.repository.MarksRepository;
import com.student.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MarksService {

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private StudentRepository studentRepository;

    public Marks addMarks(Long studentId, Marks marks) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Marks newMarks = new Marks(
                null,
                student,
                marks.getSubject(),
                marks.getExamType(),
                marks.getMarks(),
                marks.getTotalMarks(),
                marks.getExamDate(),
                marks.getRemarks(),
                0.0,
                ""
        );
        return marksRepository.save(newMarks);
    }

    public List<Marks> getStudentMarks(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return marksRepository.findByStudent(student);
    }

    public List<Marks> getMarksBySubject(Long studentId, String subject) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return marksRepository.findByStudentAndSubject(student, subject);
    }

    public List<Marks> getMarksByExamType(Long studentId, String examType) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return marksRepository.findByStudentAndExamType(student, examType);
    }

    public Double getAverageMarks(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return marksRepository.getAveragePercentageByStudent(student);
    }

    public Marks updateMarks(Long marksId, Marks updatedMarks) {
        Marks marks = marksRepository.findById(marksId)
                .orElseThrow(() -> new RuntimeException("Marks not found"));
        Marks updated = Marks.builder()
                .id(marks.getId())
                .student(marks.getStudent())
                .subject(marks.getSubject())
                .examType(marks.getExamType())
                .marks(updatedMarks.getMarks() > 0 ? updatedMarks.getMarks() : marks.getMarks())
                .totalMarks(updatedMarks.getTotalMarks() > 0 ? updatedMarks.getTotalMarks() : marks.getTotalMarks())
                .remarks(updatedMarks.getRemarks() != null ? updatedMarks.getRemarks() : marks.getRemarks())
                .examDate(marks.getExamDate())
                .build();
        return marksRepository.save(updated);
    }

    public void deleteMarks(Long marksId) {
        marksRepository.deleteById(marksId);
    }
}
