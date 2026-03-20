package controller;

import dao.StudentDAO;
import model.Student;

import java.util.List;

public class StudentController {
    private final StudentDAO studentDAO;

    public StudentController() {
        this.studentDAO = new StudentDAO();
    }

    public void addStudent(String name, String email) {
        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        studentDAO.save(student);
    }

    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }
}
