package com.springbatchdemo.entity.student;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String subject;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Student> students = new ArrayList<>();

    public Teacher() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    protected static class Builder {
        private String name;
        private String subject;

        public Teacher.Builder name(String value) {
            name =value;
            return this;
        }

        public Teacher.Builder subject(String value) {
            subject =value;
            return this;
        }

        public Teacher build() {
            return new Teacher(this);
        }
    }

    private Teacher(Teacher.Builder builder) {
        name = builder.name;
        subject = builder.subject;
    }

    public static Teacher.Builder builder() {
        return new Teacher.Builder();
    }

    public void addStudent(Student student){
        students.add(student);
        student.setTeacher(this);
    }
}
