package com.hicode.restapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hicode.restapi.model.Student;
import com.hicode.restapi.service.StudentService;

@RestController
@CrossOrigin("*")
public class StudentRestController {
    

    @Autowired
    StudentService service;

    @GetMapping("students")
    public ResponseEntity< List<Student> >findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("students/{id}")
    public ResponseEntity<Student> findById(@PathVariable("id") String id) {
        if (service.findById(id)==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.findById(id));
    }
    @PostMapping("students")
    public ResponseEntity<Student> insert(@RequestBody Student student) {
        if(service.findById(student.getIdStudent())!=null) {
            return ResponseEntity.badRequest().build();
        }
        service.save(student);
        return ResponseEntity.ok(student);
    }
    @PutMapping("students/{id}")
    public ResponseEntity<Student> update(@PathVariable("id") String id, @RequestBody Student student){
        if (service.findById(id)==null) {
            return ResponseEntity.notFound().build();
        }
        service.save(student);
        return  ResponseEntity.ok(student);
    }
    @DeleteMapping("students/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        if (service.findById(id)==null){
            return ResponseEntity.notFound().build();
        }
        service.delete(service.findById(id));
        return ResponseEntity.ok().build(); //void
    }
}
