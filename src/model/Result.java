package model;

public class Result {
    private int id;
    private int enrollmentId;
    private String grade;

    public Result() {}

    public Result(int id, int enrollmentId, String grade) {
        this.id = id;
        this.enrollmentId = enrollmentId;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
