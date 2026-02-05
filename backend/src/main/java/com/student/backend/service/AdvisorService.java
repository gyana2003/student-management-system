package com.student.backend.service;

import com.student.backend.model.AdvisorSession;
import com.student.backend.model.Student;
import com.student.backend.repository.AdvisorSessionRepository;
import com.student.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdvisorService {

    @Autowired
    private AdvisorSessionRepository advisorSessionRepository;

    @Autowired
    private StudentRepository studentRepository;

    public AdvisorSession createAdvisorSession(Long studentId, AdvisorSession session) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        session.setStudent(student);
        
        // Since we're using a mock AI service, generate a basic response
        String mockAdvice = generateMockAdvice(session.getQuestion(), session.getCategory());
        session.setAdvice(mockAdvice);
        
        return advisorSessionRepository.save(session);
    }

    private String generateMockAdvice(String question, String category) {
        // Mock AI response - in production, integrate with actual AI service
        return "Based on your query about " + category + ": " +
                "Keep working hard and stay focused on your goals. " +
                "Consider consulting with your academic advisor for personalized guidance. " +
                "Original question: " + question;
    }

    public List<AdvisorSession> getStudentSessions(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return advisorSessionRepository.findByStudentOrderByCreatedAtDesc(student);
    }

    public AdvisorSession getSession(Long sessionId) {
        return advisorSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public void deleteSession(Long sessionId) {
        advisorSessionRepository.deleteById(sessionId);
    }
}
