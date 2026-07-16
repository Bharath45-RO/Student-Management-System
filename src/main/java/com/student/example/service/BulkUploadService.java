package com.student.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.student.example.entity.Student;
import com.student.example.repository.StudentRepository;

@Service
public class BulkUploadService {

    @Autowired
    private StudentRepository repository;

    public void save(MultipartFile file) {

        List<Student> students = ExcelHelper.excelToStudents(file);

        repository.saveAll(students);

    }

}