package com.student.example.controller;

import java.util.Arrays;
import java.util.List;
import com.student.example.service.BulkUploadService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.student.example.entity.Student;
import com.student.example.service.EmailService;
import com.student.example.service.ExcelExportService;
import com.student.example.service.StudentService;

import jakarta.servlet.http.HttpSession;

@Controller
public class StudentController {

    @Autowired
    private StudentService service;
    @Autowired
    private EmailService emailService;

    // ==========================
    // Home Page (Dashboard)
    // ==========================
    @GetMapping("/")
    public String home(HttpSession session, Model model) {

        // Check Login
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        List<Student> students = service.getAllStudents();

        for(Student s : students){
            System.out.println(
                s.getId() + " " + s.getName() + " " + s.getCourse()
            );
        }

        model.addAttribute("students", students);

        model.addAttribute("totalStudents", service.getTotalStudents());
        model.addAttribute("activeStudents", service.getActiveStudents());
        model.addAttribute("maleStudents", service.getMaleStudents());
        model.addAttribute("femaleStudents", service.getFemaleStudents());

        return "index";
    }

    // ==========================
    // Add Student Page
    // ==========================
    @GetMapping("/add")
    public String addStudent(HttpSession session, Model model) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        model.addAttribute("student", new Student());

        return "add";
    }

    // ==========================
    // Save Student
    // ==========================
    @PostMapping("/save")
    public String saveStudent(@ModelAttribute Student student,
                              @RequestParam(value = "image", required = false) MultipartFile image)
            throws IOException {

        if (image != null && !image.isEmpty()) {

            String uploadDir = System.getProperty("user.dir")
                    + "/src/main/resources/static/images/";

            File dir = new File(uploadDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = System.currentTimeMillis()
                    + "_" + image.getOriginalFilename();

            File saveFile = new File(uploadDir + fileName);

            image.transferTo(saveFile);

            student.setPhoto(fileName);
        }

        // Save student
        service.saveStudent(student);

        // Print recipient in console (for testing)
        System.out.println("Sending email to: " + student.getEmail());

        // Send welcome email
        emailService.sendWelcomeEmail(
                student.getEmail(),
                student.getName());

        return "redirect:/";
    }
    // Edit Student
    // ==========================
    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable Integer id,
                              Model model,
                              HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        Student student = service.getStudentById(id);

        model.addAttribute("student", student);

        return "edit";
    }
    // ==========================
    // Delete Student
    // ==========================
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Integer id,
                                HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        Student student = service.getStudentById(id);

        if (student != null && student.getPhoto() != null) {

            File file = new File("src/main/resources/static/uploads/" + student.getPhoto());

            if (file.exists()) {

                file.delete();

            }

        }

        service.deleteStudent(id);

        return "redirect:/";
    }

    // ==========================
    // Search Student
    // ==========================
    @GetMapping("/search")
    public String searchStudent(@RequestParam String keyword,
                                HttpSession session,
                                Model model) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        List<Student> students = service.searchByName(keyword);

        model.addAttribute("students", students);

        model.addAttribute("totalStudents", service.getTotalStudents());
        model.addAttribute("activeStudents", service.getActiveStudents());
        model.addAttribute("maleStudents", service.getMaleStudents());
        model.addAttribute("femaleStudents", service.getFemaleStudents());

        return "index";
    }

    // ==========================
    // Filter By Class
    // ==========================
    @GetMapping("/filter/class")
    public String filterByClass(@RequestParam String studentClass,
                                HttpSession session,
                                Model model) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        model.addAttribute("students",
                service.filterByClass(studentClass));

        model.addAttribute("totalStudents", service.getTotalStudents());
        model.addAttribute("activeStudents", service.getActiveStudents());
        model.addAttribute("maleStudents", service.getMaleStudents());
        model.addAttribute("femaleStudents", service.getFemaleStudents());

        return "index";
    }

    // ==========================
    // Filter By Status
    // ==========================
    @GetMapping("/filter/status")
    public String filterByStatus(@RequestParam String status,
                                 HttpSession session,
                                 Model model) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        model.addAttribute("students",
                service.filterByStatus(status));

        model.addAttribute("totalStudents", service.getTotalStudents());
        model.addAttribute("activeStudents", service.getActiveStudents());
        model.addAttribute("maleStudents", service.getMaleStudents());
        model.addAttribute("femaleStudents", service.getFemaleStudents());

        return "index";
    }
    @Autowired
    private BulkUploadService bulkUploadService;
    @GetMapping("/bulk-upload")
    public String bulkUploadPage(HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        return "bulk-upload";
    }
    @PostMapping("/bulk-upload")
    public String bulkUpload(@RequestParam("file") MultipartFile file,
                             HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        bulkUploadService.save(file);

        return "redirect:/";
    }
    @Autowired
    private ExcelExportService excelExportService;
    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportStudents() throws IOException {

        List<Student> students = service.getAllStudents();

        ByteArrayInputStream in = excelExportService.export(students);

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition",
                "attachment; filename=students.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(in));
    }
    @PostMapping("/bulk-enroll")
    public String bulkEnroll(@RequestParam String ids,
                             @RequestParam String course) {

        List<Integer> list = Arrays.stream(ids.split(","))
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .toList();

        service.bulkEnroll(list, course);

        return "redirect:/?refresh=" + System.currentTimeMillis();
    }
}