package com.student.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.example.entity.Admin;
import com.student.example.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository repository;

    public Admin login(String username, String password) {
        return repository.findByUsernameAndPassword(username, password);
    }

    public void updatePassword(String email, String password) {
        Admin admin = repository.findByEmail(email);

        if (admin != null) {
            admin.setPassword(password);
            repository.save(admin);
        }
    }
}