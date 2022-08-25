package com.hicode.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hicode.restapi.model.Student;

public interface StudentRepository extends JpaRepository<Student, String> {
    
}
