package com.student.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Welcome Email
    public void sendWelcomeEmail(String to, String name) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Welcome to Student Management System");

        message.setText(
                "Dear " + name + ",\n\n" +
                "Your student account has been created successfully.\n\n" +
                "Thank you.\nStudent Management System");

        mailSender.send(message);
    }

    // Forgot Password Email
    public void sendResetPasswordEmail(String to) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Reset Password");

        message.setText(
                "Click the link below to reset your password:\n\n" +
                "http://localhost:8080/reset-password?email=" + to);

        mailSender.send(message);
    }
}