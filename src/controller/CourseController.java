package controller;

import dao.CourseDAO;
import model.Course;

import java.util.List;

public class CourseController {
    private final CourseDAO courseDAO;

    public CourseController() {
        this.courseDAO = new CourseDAO();
    }

    public void addCourse(String name, int credits) {
        Course course = new Course();
        course.setName(name);
        course.setCredits(credits);
        courseDAO.save(course);
    }

    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }
}
