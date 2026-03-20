package view;

import controller.AuthController;
import controller.CourseController;
import controller.EnrollmentController;
import controller.StudentController;
import model.Course;
import model.Enrollment;
import model.Student;
import model.User;

import java.util.List;
import java.util.Scanner;

public class MainView {
    private final StudentController studentController;
    private final CourseController courseController;
    private final EnrollmentController enrollmentController;
    private final AuthController authController;

    public MainView(StudentController studentController, CourseController courseController, EnrollmentController enrollmentController, AuthController authController) {
        this.studentController = studentController;
        this.courseController = courseController;
        this.enrollmentController = enrollmentController;
        this.authController = authController;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Username: ");
        String user = scanner.nextLine().trim();
        System.out.print("Password: ");
        String pass = scanner.nextLine().trim();

        User loggedInUser = authController.authenticate(user, pass);
        if (loggedInUser == null) {
            System.out.println("Invalid credentials. Use admin/admin.");
            return;
        }

        while (true) {
            System.out.println("\n--- Student Management System ---");
            System.out.println("1) Add student");
            System.out.println("2) List students");
            System.out.println("3) Add course");
            System.out.println("4) List courses");
            System.out.println("5) Enroll student");
            System.out.println("6) List enrollments");
            if ("teacher".equalsIgnoreCase(loggedInUser.getRole())) {
                System.out.println("Teacher mode: read-only access in desktop app.");
            }
            System.out.println("0) Exit");
            System.out.print("Select: ");

            String option = scanner.nextLine().trim();
            switch (option) {
                case "1" -> addStudent(scanner);
                case "2" -> listStudents();
                case "3" -> addCourse(scanner);
                case "4" -> listCourses();
                case "5" -> enrollStudent(scanner);
                case "6" -> listEnrollments();
                case "0" -> {
                    System.out.println("Goodbye.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void addStudent(Scanner scanner) {
        System.out.print("Student name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        studentController.addStudent(name, email);
        System.out.println("Added student.");
    }

    private void listStudents() {
        List<Student> students = studentController.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students.");
            return;
        }
        System.out.println("Students:");
        for (Student s : students) {
            System.out.println(s);
        }
    }

    private void addCourse(Scanner scanner) {
        System.out.print("Course name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Credits: ");
        int c = Integer.parseInt(scanner.nextLine().trim());
        courseController.addCourse(name, c);
        System.out.println("Added course.");
    }

    private void listCourses() {
        List<Course> courses = courseController.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses.");
            return;
        }
        System.out.println("Courses:");
        for (Course c : courses) {
            System.out.println(c);
        }
    }

    private void enrollStudent(Scanner scanner) {
        System.out.print("Student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Course ID: ");
        int courseId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Semester: ");
        String semester = scanner.nextLine().trim();
        enrollmentController.enrollStudent(studentId, courseId, semester);
        System.out.println("Student enrolled.");
    }

    private void listEnrollments() {
        List<Enrollment> enrollments = enrollmentController.getAllEnrollments();
        if (enrollments.isEmpty()) {
            System.out.println("No enrollments.");
            return;
        }
        System.out.println("Enrollments:");
        for (Enrollment e : enrollments) {
            System.out.println(e);
        }
    }
}
