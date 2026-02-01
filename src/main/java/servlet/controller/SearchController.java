package servlet.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.beans.Student;
import model.beans.Subject;
import model.beans.Teacher;
import model.dao.ClassDAO;
import model.dao.GradeDAO;
import model.dao.StudentDAO;
import model.dao.SubjectDAO;
import model.dao.TeacherDAO;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Aggregated search across students, teachers, subjects and classes.
 */
@WebServlet("/search")
public class SearchController extends HttpServlet {

    private StudentDAO studentDAO;
    private TeacherDAO teacherDAO;
    private SubjectDAO subjectDAO;
    private ClassDAO classDAO;
    private GradeDAO gradeDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
        teacherDAO = new TeacherDAO();
        subjectDAO = new SubjectDAO();
        classDAO = new ClassDAO();
        gradeDAO = new GradeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("q");
        HttpSession session = req.getSession(false);
        String role = session != null ? (String) session.getAttribute("role") : null;

        Map<Integer, Double> studentAverages = new HashMap<>();
        if (keyword != null && !keyword.isBlank()) {
            List<Student> students = studentDAO.searchStudents(keyword);
            List<Teacher> teachers = teacherDAO.searchTeachers(keyword);
            List<Subject> subjects = subjectDAO.searchSubjects(keyword);
            List<String> classes = classDAO.searchClasses(keyword);

            req.setAttribute("studentResults", students);
            req.setAttribute("teacherResults", teachers);
            req.setAttribute("subjectResults", subjects);
            req.setAttribute("classResults", classes);

            if ("PARENT".equalsIgnoreCase(role) && students != null) {
                for (Student student : students) {
                    Double avg = gradeDAO.getAverageForStudent(student.getId());
                    studentAverages.put(student.getId(), avg);
                }
            }
        }
        req.setAttribute("studentAverages", studentAverages);
        req.setAttribute("keyword", keyword);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/search.jsp");
        dispatcher.forward(req, resp);
    }
}

