package model.dao;

import model.beans.Teacher;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherDAO {

    // Insert new teacher
    public boolean insertTeacher(Teacher t) {
        String sql = "INSERT INTO teachers (name, surname, omang_or_passport, gender, dob, address, contact_no, email, qualifications, subjects_qualified, date_joined, class_to_subject) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, t.getName());
            ps.setString(2, t.getSurname());
            ps.setString(3, t.getOmangOrPassport());
            ps.setString(4, t.getGender());
            ps.setDate(5, t.getDob() != null ? Date.valueOf(t.getDob()) : null);
            ps.setString(6, t.getAddress());
            ps.setString(7, t.getContactNo());
            ps.setString(8, t.getEmail());
            ps.setString(9, t.getQualifications());
            ps.setString(10, t.getSubjectsQualified() != null ? String.join(",", t.getSubjectsQualified()) : null);
            ps.setDate(11, t.getDateJoined() != null ? Date.valueOf(t.getDateJoined()) : Date.valueOf(LocalDate.now()));
            ps.setString(12, t.getClassToSubject() != null ? t.getClassToSubject().toString() : null);

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                t.setId(keys.getInt(1));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve all teachers
    public List<Teacher> getAllTeachers() {
        List<Teacher> list = new ArrayList<>();
        String sql = "SELECT * FROM teachers ORDER BY name ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Teacher t = mapTeacher(rs);
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Retrieve teacher by ID
    public Teacher getTeacherById(int id) {
        String sql = "SELECT * FROM teachers WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapTeacher(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper to map ResultSet -> Teacher
    private Teacher mapTeacher(ResultSet rs) throws SQLException {
        Teacher t = new Teacher();
        t.setId(rs.getInt("id"));
        t.setName(rs.getString("name"));
        t.setSurname(rs.getString("surname"));
        t.setOmangOrPassport(rs.getString("omang_or_passport"));
        t.setGender(rs.getString("gender"));

        Date d = rs.getDate("dob");
        if (d != null) t.setDob(d.toLocalDate());

        t.setAddress(rs.getString("address"));
        t.setContactNo(rs.getString("contact_no"));
        t.setEmail(rs.getString("email"));
        t.setQualifications(rs.getString("qualifications"));

        String subjects = rs.getString("subjects_qualified");
        if (subjects != null && !subjects.isEmpty()) {
            t.setSubjectsQualified(List.of(subjects.split(",")));
        }

        Date joined = rs.getDate("date_joined");
        if (joined != null) t.setDateJoined(joined.toLocalDate());

        String mapString = rs.getString("class_to_subject");
        if (mapString != null && mapString.startsWith("{")) {
            Map<String, String> map = new HashMap<>();
            // crude parse for strings like {ClassA=Math, ClassB=Science}
            mapString = mapString.substring(1, mapString.length() - 1);
            String[] entries = mapString.split(", ");
            for (String entry : entries) {
                String[] kv = entry.split("=");
                if (kv.length == 2) map.put(kv[0], kv[1]);
            }
            t.setClassToSubject(map);
        }
        return t;
    }

    // Update existing teacher (your provided code)
    public boolean updateTeacher(Teacher t) {
        String sql = "UPDATE teachers SET name=?, surname=?, omang_or_passport=?, gender=?, dob=?, address=?, contact_no=?, email=?, qualifications=?, subjects_qualified=?, date_joined=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getSurname());
            ps.setString(3, t.getOmangOrPassport());
            ps.setString(4, t.getGender());
            ps.setDate(5, t.getDob() != null ? Date.valueOf(t.getDob()) : null);
            ps.setString(6, t.getAddress());
            ps.setString(7, t.getContactNo());
            ps.setString(8, t.getEmail());
            ps.setString(9, t.getQualifications());
            ps.setString(10, t.getSubjectsQualified() != null ? String.join(",", t.getSubjectsQualified()) : null);
            ps.setDate(11, t.getDateJoined() != null ? Date.valueOf(t.getDateJoined()) : Date.valueOf(LocalDate.now()));
            ps.setInt(12, t.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Assign classes to teacher (your provided code)
    public void assignClassesToTeacher(int id, Map<String, String> classToSubject) {
        String data = classToSubject.toString();
        String sql = "UPDATE teachers SET class_to_subject=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, data);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete teacher by ID
    public boolean deleteTeacher(int id) {
        String sql = "DELETE FROM teachers WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    void main() { }

    public int countTeachers() {
        String sql = "SELECT COUNT(*) AS total FROM teachers";
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

    public List<Teacher> searchTeachers(String keyword) {
        List<Teacher> list = new ArrayList<>();
        String sql = "SELECT * FROM teachers WHERE LOWER(name) LIKE ? OR LOWER(surname) LIKE ? OR LOWER(subjects_qualified) LIKE ?";
        String term = "%" + keyword.toLowerCase() + "%";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, term);
            ps.setString(2, term);
            ps.setString(3, term);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapTeacher(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get teachers who teach a specific subject (by subject name).
     * Checks both subjects_qualified and class_to_subject mappings.
     */
    public List<Teacher> getTeachersBySubject(String subjectName) {
        List<Teacher> list = new ArrayList<>();
        String sql = "SELECT DISTINCT t.* FROM teachers t " +
                "WHERE LOWER(t.subjects_qualified) LIKE ? " +
                "OR t.class_to_subject LIKE ? " +
                "ORDER BY t.name ASC";
        String term = "%" + subjectName.toLowerCase() + "%";
        String mapTerm = "%" + subjectName + "%";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, term);
            ps.setString(2, mapTerm);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapTeacher(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get teachers who teach a specific subject (by subject ID).
     * Also checks grades table for teachers who have graded this subject.
     */
    public List<Teacher> getTeachersBySubjectId(int subjectId) {
        List<Teacher> list = new ArrayList<>();
        // First, get subject name
        String subjectName = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT name FROM subjects WHERE id = ?")) {
            ps.setInt(1, subjectId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                subjectName = rs.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (subjectName == null) return list;

        // Get teachers from subjects_qualified or class_to_subject
        String sql1 = "SELECT DISTINCT t.* FROM teachers t " +
                "WHERE LOWER(t.subjects_qualified) LIKE ? " +
                "OR t.class_to_subject LIKE ?";
        String term = "%" + subjectName.toLowerCase() + "%";
        String mapTerm = "%" + subjectName + "%";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql1)) {
            ps.setString(1, term);
            ps.setString(2, mapTerm);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Teacher t = mapTeacher(rs);
                if (!list.stream().anyMatch(teacher -> teacher.getId() == t.getId())) {
                    list.add(t);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Also get teachers from grades table who have graded this subject
        String sql2 = "SELECT DISTINCT t.* FROM teachers t " +
                "JOIN grades g ON t.id = g.teacher_id " +
                "WHERE g.subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql2)) {
            ps.setInt(1, subjectId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Teacher t = mapTeacher(rs);
                if (!list.stream().anyMatch(teacher -> teacher.getId() == t.getId())) {
                    list.add(t);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
