package servlet.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.beans.User;
import model.dao.UserDAO;
import util.PasswordUtil;

import java.io.IOException;

/**
 * Handles parent/user self-registration.
 */
@WebServlet("/register")
public class RegistrationController extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/registration.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirmPassword");
        String role = req.getParameter("role");

        if (fullName == null || fullName.isBlank() ||
                username == null || username.isBlank() ||
                password == null || password.isBlank()) {
            req.setAttribute("error", "All fields are required.");
            forwardBack(req, resp);
            return;
        }

        if (!password.equals(confirm)) {
            req.setAttribute("error", "Passwords do not match.");
            forwardBack(req, resp);
            return;
        }

        if (userDAO.usernameExists(username)) {
            req.setAttribute("error", "Username already exists.");
            forwardBack(req, resp);
            return;
        }

        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setPassword(PasswordUtil.hash(password));
        user.setRole(role != null && !role.isBlank() ? role : "PARENT");

        userDAO.insertUser(user);

        HttpSession session = req.getSession(true);
        session.setAttribute("flash", "Registration successful! Please log in.");
        resp.sendRedirect(req.getContextPath() + "/auth");
    }

    private void forwardBack(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/registration.jsp");
        dispatcher.forward(req, resp);
    }
}
