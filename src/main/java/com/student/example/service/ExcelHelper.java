package com.student.example.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.student.example.entity.Student;

public class ExcelHelper {

    public static List<Student> excelToStudents(MultipartFile file) {

        List<Student> students = new ArrayList<>();

        try {

            InputStream is = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheetAt(0);

            DataFormatter formatter = new DataFormatter();

            for (Row row : sheet) {

                if (row.getRowNum() == 0)
                    continue;

                Student student = new Student();

                student.setName(formatter.formatCellValue(row.getCell(0)).trim());
                student.setCourse(formatter.formatCellValue(row.getCell(1)).trim());
                student.setEmail(formatter.formatCellValue(row.getCell(2)).trim());
                student.setGender(formatter.formatCellValue(row.getCell(3)).trim());
                student.setStudentClass(formatter.formatCellValue(row.getCell(4)).trim());
                student.setStatus(formatter.formatCellValue(row.getCell(5)).trim());

                System.out.println(student.getName());
                System.out.println(student.getCourse());
                System.out.println(student.getEmail());
                System.out.println(student.getGender());
                System.out.println(student.getStudentClass());
                System.out.println(student.getStatus());

                students.add(student);
            }

            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return students;
    }
}