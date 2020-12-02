package com.springbatchdemo.entity.student;

public class ClassInformation {

    private String teacherName;
    private int studentSize;

    public ClassInformation() {
    }

    public ClassInformation(String teacherName, int studentSize) {
        this.teacherName = teacherName;
        this.studentSize = studentSize;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getStudentSize() {
        return studentSize;
    }

    public void setStudentSize(int studentSize) {
        this.studentSize = studentSize;
    }

    @Override
    public String toString() {
        return "ClassInformation{" +
                "teacherName='" + teacherName + '\'' +
                ", studentSize=" + studentSize +
                '}';
    }
}
