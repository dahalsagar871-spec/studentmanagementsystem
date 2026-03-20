package model;

public class Enrollment {
    private int id;
    private int studentId;
    private int courseId;
    private String semester;

    public Enrollment() {}

    public Enrollment(int id, int studentId, int courseId, String semester) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.semester = semester;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return String.format("%d: studentId=%d courseId=%d semester=%s", id, studentId, courseId, semester);
    }
}
