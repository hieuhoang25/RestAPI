package com.hicode.restapi.repository;
import com.hicode.restapi.model.Subject;


import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, String> {
    
}
