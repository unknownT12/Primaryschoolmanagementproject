package servlet.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Student;
import model.dao.ClassDAO;
import model.dao.StudentDAO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@WebServlet("/students")
public class StudentController extends HttpServlet {

    private StudentDAO studentDAO;
    private ClassDAO classDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
        classDAO = new ClassDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                showForm(req, resp, null);
                break;
            case "edit":
                showEditForm(req, resp);
                break;
            case "delete":
                deleteStudent(req, resp);
                break;
            default:
                listStudents(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "insert";
        switch (action) {
            case "insert":
                insertStudent(req, resp);
                break;
            case "update":
                updateStudent(req, resp);
                break;
            case "assignClass":
                assignStudentToClass(req, resp);
                break;
            default:
                resp.sendRedirect("students");
        }
    }

    private void listStudents(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        String status = req.getParameter("status");
        String classFilter = req.getParameter("classFilter");
        String sort = req.getParameter("sort");
        List<Student> list = studentDAO.findStudents(keyword, status, classFilter, sort);
        req.setAttribute("students", list);
        req.setAttribute("classOptions", classDAO.getAllClasses());
        req.setAttribute("selectedStatus", status);
        req.setAttribute("selectedClass", classFilter);
        req.setAttribute("selectedSort", sort);
        req.setAttribute("keyword", keyword);
        RequestDispatcher dispatcher = req.getRequestDispatcher("jsp/studentList.jsp");
        dispatcher.forward(req, resp);
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, Student student)
            throws ServletException, IOException {
        req.setAttribute("student", student);
        req.setAttribute("classOptions", classDAO.getAllClasses());
        RequestDispatcher dispatcher = req.getRequestDispatcher("jsp/studentForm.jsp");
        dispatcher.forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Student existing = studentDAO.getStudentById(id);
        showForm(req, resp, existing);
    }

    private void insertStudent(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        Student s = buildStudentFromRequest(req, null);
        s.setRegistrationDate(LocalDate.now());
        s.setStatus(s.getStatus() != null ? s.getStatus() : "Active");
        studentDAO.insertStudent(s);
        resp.sendRedirect("students");
    }

    private void updateStudent(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        int id = Integer.parseInt(req.getParameter("id"));
        Student existing = studentDAO.getStudentById(id);
        if (existing == null) {
            resp.sendRedirect("students");
            return;
        }
        Student updated = buildStudentFromRequest(req, existing);
        updated.setId(id);
        updated.setRegistrationDate(existing.getRegistrationDate());
        studentDAO.updateStudent(updated);
        resp.sendRedirect("students");
    }

    private void deleteStudent(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        studentDAO.deleteStudent(id);
        resp.sendRedirect("students");
    }

    private void assignStudentToClass(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int studentId = Integer.parseInt(req.getParameter("studentId"));
        String className = req.getParameter("className");
        studentDAO.updateStudentClass(studentId, className);
        resp.sendRedirect(req.getContextPath() + "/studentDetails?id=" + studentId);
    }

    private Student buildStudentFromRequest(HttpServletRequest req, Student base) {
        Student s = base != null ? base : new Student();
        s.setName(req.getParameter("name"));
        s.setSurname(req.getParameter("surname"));
        s.setBirthCertificateNo(req.getParameter("birthCertificateNo"));
        s.setGender(req.getParameter("gender"));
        String dob = req.getParameter("dob");
        if (dob != null && !dob.isBlank()) s.setDob(LocalDate.parse(dob));
        s.setAddress(req.getParameter("address"));
        s.setGuardianName(req.getParameter("guardianName"));
        s.setGuardianContact(req.getParameter("guardianContact"));
        s.setGuardianEmail(req.getParameter("guardianEmail"));
        s.setStatus(req.getParameter("status"));
        String classValue = req.getParameter("schoolClass");
        if ((classValue == null || classValue.isBlank()) && req.getParameter("class") != null) {
            classValue = req.getParameter("class");
        }
        s.setSchoolClass(classValue);
        s.setRemarks(req.getParameter("remarks"));
        return s;
    }
}
