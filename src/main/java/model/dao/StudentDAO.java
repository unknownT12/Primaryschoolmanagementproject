package model.dao;

import model.beans.Student;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles all CRUD operations for the 'students' table.
 */
public class StudentDAO {

    // CREATE
    public boolean insertStudent(Student s) {
        String sql = "INSERT INTO students (name, surname, birth_certificate_no, gender, dob, address, " +
                "guardian_name, guardian_contact, guardian_email, registration_date, status, class, remarks) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getSurname());
            ps.setString(3, s.getBirthCertificateNo());
            ps.setString(4, s.getGender());
            setDateOrNull(ps, 5, s.getDob());
            ps.setString(6, s.getAddress());
            ps.setString(7, s.getGuardianName());
            ps.setString(8, s.getGuardianContact());
            ps.setString(9, s.getGuardianEmail());
            setDateOrNull(ps, 10, s.getRegistrationDate() != null ? s.getRegistrationDate() : LocalDate.now());
            ps.setString(11, s.getStatus());
            ps.setString(12, s.getSchoolClass());
            ps.setString(13, s.getRemarks());

            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                s.setId(keys.getInt(1));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ (list all)
    public List<Student> getAllStudents() {
        return findStudents(null, null, null, null);
    }

    public List<Student> findStudents(String keyword, String status, String className, String sortField) {
        List<Student> students = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM students WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (LOWER(name) LIKE ? OR LOWER(surname) LIKE ? OR LOWER(birth_certificate_no) LIKE ?)");
            String term = "%" + keyword.toLowerCase() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
        }
        if (status != null && !status.isBlank()) {
            sql.append(" AND status = ?");
            params.add(status);
        }
        if (className != null && !className.isBlank()) {
            sql.append(" AND class = ?");
            params.add(className);
        }

        sql.append(" ORDER BY ");
        switch (sortField != null ? sortField : "") {
            case "class":
                sql.append("class ASC, surname ASC");
                break;
            case "status":
                sql.append("status ASC, surname ASC");
                break;
            case "recent":
                sql.append("registration_date DESC, id DESC");
                break;
            default:
                sql.append("surname ASC, name ASC");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(mapStudent(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    // READ by ID
    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapStudent(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    public boolean updateStudent(Student s) {
        String sql = "UPDATE students SET name=?, surname=?, birth_certificate_no=?, gender=?, dob=?, address=?, " +
                "guardian_name=?, guardian_contact=?, guardian_email=?, registration_date=?, status=?, class=?, remarks=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getSurname());
            ps.setString(3, s.getBirthCertificateNo());
            ps.setString(4, s.getGender());
            setDateOrNull(ps, 5, s.getDob());
            ps.setString(6, s.getAddress());
            ps.setString(7, s.getGuardianName());
            ps.setString(8, s.getGuardianContact());
            ps.setString(9, s.getGuardianEmail());
            setDateOrNull(ps, 10, s.getRegistrationDate());
            ps.setString(11, s.getStatus());
            ps.setString(12, s.getSchoolClass());
            ps.setString(13, s.getRemarks());
            ps.setInt(14, s.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStudentClass(int studentId, String className) {
        String sql = "UPDATE students SET class=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, className);
            ps.setInt(2, studentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // SEARCH
    public List<Student> searchStudents(String keyword) {
        return findStudents(keyword, null, null, null);
    }

    public int countStudents() {
        String sql = "SELECT COUNT(*) AS total FROM students";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<String, Long> getStatusSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        String sql = "SELECT status, COUNT(*) AS total FROM students GROUP BY status";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                summary.put(rs.getString("status"), rs.getLong("total"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return summary;
    }

    public List<Student> getRecentStudents(int limit) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY registration_date DESC, id DESC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(mapStudent(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public List<Student> getStudentsByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            placeholders.append("?");
            if (i < ids.size() - 1) placeholders.append(",");
        }
        String sql = "SELECT * FROM students WHERE id IN (" + placeholders + ")";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 1, ids.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(mapStudent(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    private Student mapStudent(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getInt("id"));
        s.setName(rs.getString("name"));
        s.setSurname(rs.getString("surname"));
        s.setBirthCertificateNo(rs.getString("birth_certificate_no"));
        s.setGender(rs.getString("gender"));
        java.sql.Date dob = rs.getDate("dob");
        if (dob != null) s.setDob(dob.toLocalDate());
        s.setAddress(rs.getString("address"));
        s.setGuardianName(rs.getString("guardian_name"));
        s.setGuardianContact(rs.getString("guardian_contact"));
        s.setGuardianEmail(rs.getString("guardian_email"));
        java.sql.Date reg = rs.getDate("registration_date");
        if (reg != null) s.setRegistrationDate(reg.toLocalDate());
        s.setStatus(rs.getString("status"));
        s.setSchoolClass(rs.getString("class"));
        s.setRemarks(rs.getString("remarks"));
        return s;
    }

    private void setDateOrNull(PreparedStatement ps, int index, LocalDate date) throws SQLException {
        if (date != null) {
            ps.setDate(index, java.sql.Date.valueOf(date));
        } else {
            ps.setNull(index, Types.DATE);
        }
    }
}
