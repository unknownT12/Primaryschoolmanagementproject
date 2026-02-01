package servlet.controller;

import model.beans.Teacher;
import model.dao.ClassDAO;
import model.dao.TeacherDAO;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@WebServlet("/teachers")
public class TeacherController extends HttpServlet {
    private TeacherDAO teacherDAO;
    private ClassDAO classDAO;

    @Override
    public void init() {
        teacherDAO = new TeacherDAO();
        classDAO = new ClassDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                showForm(req, resp, null);
                break;
            case "edit":
                editTeacher(req, resp);
                break;
            case "delete":
                deleteTeacher(req, resp);
                break;
            case "assign":
                assignClassesForm(req, resp);
                break;
            default:
                listTeachers(req, resp);
                break;
        }
    }

    private void listTeachers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String keyword = req.getParameter("q");
        List<Teacher> teachers = (keyword != null && !keyword.isBlank())
                ? teacherDAO.searchTeachers(keyword)
                : teacherDAO.getAllTeachers();
        req.setAttribute("teachers", teachers);
        req.setAttribute("keyword", keyword);
        RequestDispatcher dispatcher = req.getRequestDispatcher("jsp/teacherList.jsp");
        dispatcher.forward(req, resp);
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, Teacher existing)
            throws ServletException, IOException {
        req.setAttribute("teacher", existing); // Always set, even if null
        RequestDispatcher dispatcher = req.getRequestDispatcher("jsp/teacherForm.jsp");
        dispatcher.forward(req, resp);
    }

    private void editTeacher(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String idParam = req.getParameter("id");
            if (idParam == null || idParam.isBlank()) {
                resp.sendRedirect("teachers?error=Invalid+teacher+ID");
                return;
            }
            int id = Integer.parseInt(idParam);
            Teacher t = teacherDAO.getTeacherById(id);
            if (t == null) {
                resp.sendRedirect("teachers?error=Teacher+not+found");
                return;
            }
            showForm(req, resp, t);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.sendRedirect("teachers?error=Invalid+teacher+ID");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("teachers?error=Error+loading+teacher");
        }
    }

    private void deleteTeacher(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        teacherDAO.deleteTeacher(id);
        resp.sendRedirect("teachers");
    }

    private void assignClassesForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Teacher t = teacherDAO.getTeacherById(id);
        req.setAttribute("teacher", t);
        req.setAttribute("classOptions", classDAO.getAllClasses());
        RequestDispatcher dispatcher = req.getRequestDispatcher("jsp/assignClasses.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String action = req.getParameter("action");

        if ("save".equals(action)) {
            saveTeacher(req);
        } else if ("assign".equals(action)) {
            assignClasses(req);
        }
        resp.sendRedirect("teachers");
    }

    private void saveTeacher(HttpServletRequest req) {
        try {
            int id = req.getParameter("id") != null && !req.getParameter("id").isEmpty()
                    ? Integer.parseInt(req.getParameter("id")) : 0;
            Teacher t = id > 0 ? teacherDAO.getTeacherById(id) : new Teacher();
            
            // If updating and teacher not found, create new one
            if (id > 0 && t == null) {
                t = new Teacher();
                t.setId(id);
            }

            t.setName(req.getParameter("name"));
            t.setSurname(req.getParameter("surname"));
            t.setOmangOrPassport(req.getParameter("omangOrPassport"));
            t.setGender(req.getParameter("gender"));
            String dobStr = req.getParameter("dob");
            if (dobStr != null && !dobStr.isEmpty()) {
                t.setDob(LocalDate.parse(dobStr));
            }
            t.setAddress(req.getParameter("address"));
            t.setContactNo(req.getParameter("contactNo"));
            t.setEmail(req.getParameter("email"));
            t.setQualifications(req.getParameter("qualifications"));

            String subjects = req.getParameter("subjectsQualified");
            if (subjects != null && !subjects.isEmpty()) {
                t.setSubjectsQualified(Arrays.stream(subjects.split(","))
                        .map(String::trim)
                        .filter(part -> !part.isEmpty())
                        .toList());
            } else {
                t.setSubjectsQualified(null);
            }

            String dateJoined = req.getParameter("dateJoined");
            if (dateJoined != null && !dateJoined.isBlank()) {
                t.setDateJoined(LocalDate.parse(dateJoined));
            } else if (t.getDateJoined() == null) {
                t.setDateJoined(LocalDate.now());
            }

            if (id > 0) {
                teacherDAO.updateTeacher(t);
            } else {
                teacherDAO.insertTeacher(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void assignClasses(HttpServletRequest req) {
        int id = Integer.parseInt(req.getParameter("id"));
        String[] classes = req.getParameterValues("className");
        String[] subjects = req.getParameterValues("subjectName");

        if (classes != null && subjects != null && classes.length == subjects.length) {
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < classes.length; i++) {
                if (classes[i] != null && !classes[i].isBlank() &&
                        subjects[i] != null && !subjects[i].isBlank()) {
                    map.put(classes[i], subjects[i]);
                }
            }
            teacherDAO.assignClassesToTeacher(id, map);
        }
    }
}
