package com.hicode.restapi.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    private String idStudent;
    private String email;
    private String fullName;
    private boolean gender;
    private String country;

    @OneToMany(mappedBy = "student")
    private List<Subject> subjects;
}
