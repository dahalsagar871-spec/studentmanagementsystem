package controller;

import dao.EnrollmentDAO;
import model.Enrollment;

import java.util.List;

public class EnrollmentController {
    private final EnrollmentDAO enrollmentDAO;

    public EnrollmentController() {
        this.enrollmentDAO = new EnrollmentDAO();
    }

    public void enrollStudent(int studentId, int courseId, String semester) {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setSemester(semester);
        enrollmentDAO.save(enrollment);
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentDAO.findAll();
    }
}
