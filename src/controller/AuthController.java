package controller;

import dao.UserDAO;
import model.User;

public class AuthController {
    private final UserDAO userDAO;

    public AuthController() {
        userDAO = new UserDAO();
    }

    public boolean authenticate(String username, String password) {
        User user = userDAO.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }
}
