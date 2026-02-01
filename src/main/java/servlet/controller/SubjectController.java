package servlet.controller;

import model.beans.Subject;
import model.beans.Teacher;
import model.dao.GradeDAO;
import model.dao.SubjectDAO;
import model.dao.TeacherDAO;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Handles subject management (list and add).
 */
@WebServlet("/subjects")
public class SubjectController extends HttpServlet {

    private SubjectDAO subjectDAO;
    private TeacherDAO teacherDAO;
    private GradeDAO gradeDAO;

    @Override
    public void init() {
        subjectDAO = new SubjectDAO();
        teacherDAO = new TeacherDAO();
        gradeDAO = new GradeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        // Check if viewing subject details
        String subjectIdParam = req.getParameter("id");
        if (subjectIdParam != null && !subjectIdParam.isBlank()) {
            showSubjectDetails(Integer.parseInt(subjectIdParam), req, resp, role);
            return;
        }

        // List subjects
        String query = req.getParameter("q");
        List<Subject> list = (query != null && !query.isBlank())
                ? subjectDAO.searchSubjects(query)
                : subjectDAO.getAllSubjects();
        req.setAttribute("subjects", list);
        req.setAttribute("query", query);

        // Route to parent or admin/teacher view (case-insensitive check)
        if (role != null && "PARENT".equalsIgnoreCase(role)) {
            RequestDispatcher dispatcher = req.getRequestDispatcher("jsp/subjectListParent.jsp");
            dispatcher.forward(req, resp);
        } else {
            RequestDispatcher dispatcher = req.getRequestDispatcher("jsp/subjectList.jsp");
            dispatcher.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        // Parents cannot add/delete subjects (case-insensitive check)
        if (role != null && "PARENT".equalsIgnoreCase(role)) {
            resp.sendRedirect("subjects");
            return;
        }

        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            subjectDAO.deleteSubject(id);
        } else {
            String name = req.getParameter("name");
            String desc = req.getParameter("description");
            Subject s = new Subject(name, desc);
            subjectDAO.insertSubject(s);
        }
        resp.sendRedirect("subjects");
    }

    private void showSubjectDetails(int subjectId, HttpServletRequest req, HttpServletResponse resp, String role)
            throws ServletException, IOException {
        Subject subject = subjectDAO.getSubjectById(subjectId);
        if (subject == null) {
            resp.sendRedirect(req.getContextPath() + "/subjects");
            return;
        }

        List<Teacher> teachers = teacherDAO.getTeachersBySubjectId(subjectId);
        Double averageGrade = gradeDAO.getAverageForSubject(subjectId);
        int studentCount = gradeDAO.getStudentCountForSubject(subjectId);
        int gradeCount = gradeDAO.getGradeCountForSubject(subjectId);

        req.setAttribute("subject", subject);
        req.setAttribute("teachers", teachers);
        req.setAttribute("averageGrade", averageGrade);
        req.setAttribute("studentCount", studentCount);
        req.setAttribute("gradeCount", gradeCount);

        if (role != null && "PARENT".equalsIgnoreCase(role)) {
            RequestDispatcher dispatcher = req.getRequestDispatcher("jsp/subjectDetailsParent.jsp");
            dispatcher.forward(req, resp);
        } else {
            // For admin/teacher, show detailed view with students
            List<java.util.Map<String, Object>> students = gradeDAO.getStudentsBySubject(subjectId);
            req.setAttribute("students", students);
            RequestDispatcher dispatcher = req.getRequestDispatcher("jsp/subjectDetails.jsp");
            dispatcher.forward(req, resp);
        }
    }
}
