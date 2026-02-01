package servlet.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.ClassDAO;
import model.dao.GradeDAO;
import model.dao.StudentDAO;
import model.dao.TeacherDAO;

import java.io.IOException;

/**
 * Main dashboard for authenticated users.
 */
@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {

    private StudentDAO studentDAO;
    private TeacherDAO teacherDAO;
    private ClassDAO classDAO;
    private GradeDAO gradeDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
        teacherDAO = new TeacherDAO();
        classDAO = new ClassDAO();
        gradeDAO = new GradeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/auth");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (role == null) role = "ADMIN";

        int studentCount = studentDAO.countStudents();
        int classCount = classDAO.countClasses();
        req.setAttribute("studentCount", studentCount);
        req.setAttribute("classCount", classCount);
        req.setAttribute("recentStudents", studentDAO.getRecentStudents(5));
        req.setAttribute("statusSummary", studentDAO.getStatusSummary());
        req.setAttribute("topPerformers", gradeDAO.getTopStudentsOverall(5));

        switch (role.toUpperCase()) {
            case "TEACHER":
                req.getRequestDispatcher("/jsp/dashboardTeacher.jsp").forward(req, resp);
                break;
            case "PARENT":
                req.getRequestDispatcher("/jsp/dashboardParent.jsp").forward(req, resp);
                break;
            default:
                req.setAttribute("teacherCount", teacherDAO.countTeachers());
                req.getRequestDispatcher("/jsp/dashboard.jsp").forward(req, resp);
                break;
        }
    }
}
