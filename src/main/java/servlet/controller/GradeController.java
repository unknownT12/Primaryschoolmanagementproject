package servlet.controller;

import model.beans.Grade;
import model.dao.GradeDAO;
import model.dao.StudentDAO;
import model.dao.SubjectDAO;
import model.dao.TeacherDAO;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/grades")
public class GradeController extends HttpServlet {
    private GradeDAO gradeDAO;

    private SubjectDAO subjectDAO;
    private TeacherDAO teacherDAO;
    private StudentDAO studentDAO;

    @Override
    public void init() {
        gradeDAO = new GradeDAO();
        subjectDAO = new SubjectDAO();
        teacherDAO = new TeacherDAO();
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("students", studentDAO.getAllStudents());
        req.setAttribute("subjects", subjectDAO.getAllSubjects());
        req.setAttribute("teachers", teacherDAO.getAllTeachers());
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/gradeForm.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Grade g = new Grade();
            g.setStudentId(Integer.parseInt(req.getParameter("studentId")));
            g.setSubjectId(Integer.parseInt(req.getParameter("subjectId")));
            String teacherId = req.getParameter("teacherId");
            if (teacherId != null && !teacherId.isBlank()) g.setTeacherId(Integer.parseInt(teacherId));
            g.setMark(Double.parseDouble(req.getParameter("mark")));
            g.setTerm(req.getParameter("term"));
            gradeDAO.insertGrade(g);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String redirect = req.getParameter("redirectTo");
        if (redirect != null && !redirect.isBlank()) {
            resp.sendRedirect(redirect);
        } else {
            resp.sendRedirect(req.getContextPath() + "/grades");
        }
    }
}
