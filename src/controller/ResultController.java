package controller;

import dao.ResultDAO;
import model.Result;

import java.util.List;

public class ResultController {
    private final ResultDAO resultDAO;

    public ResultController() {
        this.resultDAO = new ResultDAO();
    }

    public void addResult(int enrollmentId, String grade) {
        Result result = new Result();
        result.setEnrollmentId(enrollmentId);
        result.setGrade(grade);
        resultDAO.save(result);
    }

    public List<Result> getAllResults() {
        return resultDAO.findAll();
    }
}
