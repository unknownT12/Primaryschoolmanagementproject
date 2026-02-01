package servlet.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.beans.Grade;
import model.beans.Student;
import model.dao.ClassDAO;
import model.dao.GradeDAO;
import model.dao.StudentDAO;
import model.dao.SubjectDAO;
import model.dao.TeacherDAO;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/studentDetails")
public class StudentDetailsController extends HttpServlet {
    private StudentDAO studentDAO;
    private GradeDAO gradeDAO;
    private SubjectDAO subjectDAO;
    private TeacherDAO teacherDAO;
    private ClassDAO classDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
        gradeDAO = new GradeDAO();
        subjectDAO = new SubjectDAO();
        teacherDAO = new TeacherDAO();
        classDAO = new ClassDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sid = req.getParameter("id");
        if (sid == null) {
            resp.sendRedirect(req.getContextPath() + "/students");
            return;
        }
        int id = Integer.parseInt(sid);
        Student s = studentDAO.getStudentById(id);
        if (s == null) {
            resp.sendRedirect(req.getContextPath() + "/students");
            return;
        }
        List<Grade> grades = gradeDAO.getGradesByStudent(id);
        Double avg = gradeDAO.getAverageForStudent(id);

        List<String> enrolledSubjects = grades.stream()
                .map(Grade::getSubjectName)
                .filter(name -> name != null && !name.isBlank())
                .distinct()
                .collect(Collectors.toList());

        req.setAttribute("student", s);
        req.setAttribute("grades", grades);
        req.setAttribute("average", avg);
        req.setAttribute("subjects", subjectDAO.getAllSubjects());
        req.setAttribute("teachers", teacherDAO.getAllTeachers());
        req.setAttribute("classOptions", classDAO.getAllClasses());
        req.setAttribute("enrolledSubjects", enrolledSubjects);

        String view = "/jsp/studentDetails.jsp";
        HttpSession session = req.getSession(false);
        String role = session != null ? (String) session.getAttribute("role") : null;
        if ("PARENT".equalsIgnoreCase(role)) {
            view = "/jsp/studentDetailsParent.jsp";
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher(view);
        dispatcher.forward(req, resp);
    }
}
