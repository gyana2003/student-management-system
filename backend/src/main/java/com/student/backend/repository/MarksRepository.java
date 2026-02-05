package com.student.backend.repository;

import com.student.backend.model.Marks;
import com.student.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {
    List<Marks> findByStudent(Student student);
    List<Marks> findByStudentAndSubject(Student student, String subject);
    List<Marks> findByStudentAndExamType(Student student, String examType);
    
    @Query("SELECT AVG(m.percentage) FROM Marks m WHERE m.student = ?1")
    Double getAveragePercentageByStudent(Student student);
    
    @Query("SELECT AVG(m.percentage) FROM Marks m WHERE m.subject = ?1")
    Double getAveragePercentageBySubject(String subject);
}
