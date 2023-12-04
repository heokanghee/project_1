package com.vision.haksa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vision.haksa.entitys.Student;
import com.vision.haksa.entitys.StudentRepository;

import java.util.List;

@Service
public class StudentService {
	@Autowired
	private StudentRepository studentRepository;

	public Student saveStudent(Student student) {
		return studentRepository.save(student);
	}

	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	public Student getStudentById(String id) {
		return studentRepository.findById(id).orElse(null);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteStudent(String id) {
		studentRepository.deleteById(id);
	}
}