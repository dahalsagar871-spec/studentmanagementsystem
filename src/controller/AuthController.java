package controller;

import dao.UserDAO;
import model.User;

public class AuthController {
    private final UserDAO userDAO;

    public AuthController() {
        userDAO = new UserDAO();
    }

    public User authenticate(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean registerTeacher(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return false;
        }
        if (userDAO.findByUsername(username.trim()) != null) {
            return false;
        }
        return userDAO.save(new User(0, username.trim(), password, "teacher"));
    }
}
