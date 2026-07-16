package com.student.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.student.example.entity.Admin;

import jakarta.transaction.Transactional;

public interface AdminRepository extends JpaRepository<Admin,Integer>{

    Admin findByUsernameAndPassword(String username,String password);

    Admin findByEmail(String email);

    @Transactional
    @Modifying
    @Query("update Admin a set a.password=?2 where a.email=?1")
    void updatePassword(String email,String password);
}