package com.student.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.student.example.entity.Student;

import jakarta.transaction.Transactional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    // Search by name
    List<Student> findByNameContainingIgnoreCase(String keyword);

    // Filter by class
    List<Student> findByStudentClass(String studentClass);

    // Filter by status
    List<Student> findByStatus(String status);

    // Dashboard
    long countByStatus(String status);

    long countByGender(String gender);

    // Bulk Enroll
    @Transactional
    @Modifying
    @Query("UPDATE Student s SET s.course = :course WHERE s.id IN :ids")
    void bulkEnroll(@Param("ids") List<Integer> ids,
                    @Param("course") String course);
}