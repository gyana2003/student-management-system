package com.student.backend.repository;

import com.student.backend.model.Attendance;
import com.student.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudent(Student student);
    List<Attendance> findByStudentAndAttendanceDateBetween(Student student, LocalDate startDate, LocalDate endDate);
    List<Attendance> findByStudentAndSubject(Student student, String subject);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student = ?1")
    long countByStudent(Student student);
}
