package com.student.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.student.example.entity.Student;
import com.student.example.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    // Get all students
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    // Save student
    public void saveStudent(Student student) {
        repository.save(student);
    }

    // Get student by ID
    public Student getStudentById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    // Delete student
    public void deleteStudent(Integer id) {
        repository.deleteById(id);
    }

    // Search by name
    public List<Student> searchByName(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }

    // Filter by class
    public List<Student> filterByClass(String studentClass) {
        return repository.findByStudentClass(studentClass);
    }

    // Filter by status
    public List<Student> filterByStatus(String status) {
        return repository.findByStatus(status);
    }

    // Dashboard Statistics
    public long getTotalStudents() {
        return repository.count();
    }

    public long getActiveStudents() {
        return repository.countByStatus("Active");
    }

    public long getMaleStudents() {
        return repository.countByGender("Male");
    }

    public long getFemaleStudents() {
        return repository.countByGender("Female");
    }

    @Transactional
    public void bulkEnroll(List<Integer> ids, String course) {
        repository.bulkEnroll(ids, course);
    }
}