package com.student.example.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.student.example.entity.Student;

@Service
public class ExcelExportService {

    public ByteArrayInputStream export(List<Student> students) throws IOException {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Students");

        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Course");
        header.createCell(3).setCellValue("Email");
        header.createCell(4).setCellValue("Gender");
        header.createCell(5).setCellValue("Class");
        header.createCell(6).setCellValue("Status");

        int rowNum = 1;

        for(Student s : students){

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(s.getId());

            row.createCell(1).setCellValue(s.getName());

            row.createCell(2).setCellValue(s.getCourse());

            row.createCell(3).setCellValue(s.getEmail());

            row.createCell(4).setCellValue(s.getGender());

            row.createCell(5).setCellValue(s.getStudentClass());

            row.createCell(6).setCellValue(s.getStatus());
        }

        for(int i=0;i<7;i++){
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        workbook.write(out);

        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

}