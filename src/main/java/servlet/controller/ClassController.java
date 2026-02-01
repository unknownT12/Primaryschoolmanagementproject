package servlet.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Student;
import model.dao.ClassDAO;
import model.dao.GradeDAO;
import model.beans.StudentPerformance;

import java.io.IOException;
import java.util.List;

/**
 * Displays all classes and their students.
 */
@WebServlet("/classes")
public class ClassController extends HttpServlet {

    private ClassDAO classDAO;
    private GradeDAO gradeDAO;

    @Override
    public void init() {
        classDAO = new ClassDAO();
        gradeDAO = new GradeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            deleteClass(req, resp);
            return;
        }

        String className = req.getParameter("name");
        if (className != null && !className.isBlank()) {
            showClassDetails(className, req, resp);
        } else {
            listClasses(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String className = req.getParameter("className");
        if (className != null && !className.isBlank()) {
            classDAO.addClass(className.trim());
        }
        resp.sendRedirect("classes");
    }

    private void showClassDetails(String className, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Student> students = classDAO.getStudentsByClass(className);
        List<StudentPerformance> top = gradeDAO.getTopStudentsByClass(className, 5);
        req.setAttribute("students", students);
        req.setAttribute("className", className);
        req.setAttribute("topStudents", top);
        RequestDispatcher dispatcher = req.getRequestDispatcher("jsp/classDetails.jsp");
        dispatcher.forward(req, resp);
    }

    private void listClasses(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String query = req.getParameter("q");
        List<String> classes = (query != null && !query.isBlank())
                ? classDAO.searchClasses(query)
                : classDAO.getAllClasses();
        req.setAttribute("classes", classes);
        req.setAttribute("query", query);
        req.setAttribute("classCount", classDAO.countClasses());
        RequestDispatcher dispatcher = req.getRequestDispatcher("jsp/classList.jsp");
        dispatcher.forward(req, resp);
    }

    private void deleteClass(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String className = req.getParameter("name");
        if (className != null && !className.isBlank()) {
            classDAO.deleteClass(className);
        }
        resp.sendRedirect("classes");
    }
}