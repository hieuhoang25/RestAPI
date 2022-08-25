package com.hicode.restapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hicode.restapi.model.Student;
import com.hicode.restapi.repository.StudentRepository;

@Service
public class StudentService {
    
    @Autowired
    StudentRepository dao;

    public List<Student> findAll() {
        return dao.findAll();
    }
    public  Student findById(String id) {
        return dao.findById(id).orElse(null);
    }
    public Student save(Student student) {
        dao.save(student);
        return student;
    }
    public void delete(Student student) {
        dao.delete(student);
    }

}
