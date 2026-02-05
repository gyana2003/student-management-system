package com.student.backend.repository;

import com.student.backend.model.AdvisorSession;
import com.student.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdvisorSessionRepository extends JpaRepository<AdvisorSession, Long> {
    List<AdvisorSession> findByStudent(Student student);
    List<AdvisorSession> findByStudentOrderByCreatedAtDesc(Student student);
    List<AdvisorSession> findByCategory(String category);
}
