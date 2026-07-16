package com.student.example.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.student.example.entity.Admin;
import com.student.example.service.AdminService;
import com.student.example.service.EmailService;

@Controller
public class LoginController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private EmailService emailService;

    // Login Page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Login
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Admin admin = adminService.login(username, password);

        if (admin != null) {
            session.setAttribute("user", admin.getUsername());
            return "redirect:/";
        }

        model.addAttribute("error", "Invalid Username or Password");
        return "login";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // Forgot Password Page
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    // Send Reset Email
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email,
                                 Model model) {

        emailService.sendResetPasswordEmail(email);

        model.addAttribute("message",
                "Password reset link has been sent to your email.");

        return "forgot-password";
    }

    // Reset Password Page
    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                Model model) {

        model.addAttribute("email", email);
        return "reset-password";
    }

    // Update Password
    @PostMapping("/reset-password")
    public String updatePassword(@RequestParam String email,
                                 @RequestParam String password) {

        adminService.updatePassword(email, password);

        return "redirect:/login";
    }
}