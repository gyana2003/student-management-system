package com.student.backend;

import com.student.backend.model.Student;
import com.student.backend.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class BackendApplicationTests {

	@Autowired
	private StudentRepository studentRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void testStudentModelCreation() {
		Student student = new Student();
		student.setName("John Doe");
		student.setEmail("john@example.com");
		student.setCourse("Java Programming");
		
		assertNotNull(student);
		assertEquals("John Doe", student.getName());
		assertEquals("john@example.com", student.getEmail());
		assertEquals("Java Programming", student.getCourse());
	}

	@Test
	void testStudentRepository() {
		Student student = new Student();
		student.setName("Jane Smith");
		student.setEmail("jane@example.com");
		student.setCourse("Spring Boot");
		
		Student savedStudent = studentRepository.save(student);
		assertNotNull(savedStudent.getId());
		
		Student retrievedStudent = studentRepository.findById(savedStudent.getId()).orElse(null);
		assertNotNull(retrievedStudent);
		assertEquals("Jane Smith", retrievedStudent.getName());
	}
}
