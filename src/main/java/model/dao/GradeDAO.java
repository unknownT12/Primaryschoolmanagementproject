package model.dao;

import model.beans.Grade;
import model.beans.StudentPerformance;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {

    public boolean insertGrade(Grade g) {
        String sql = "INSERT INTO grades (student_id, subject_id, teacher_id, mark, term) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, g.getStudentId());
            ps.setInt(2, g.getSubjectId());
            if (g.getTeacherId() != null) ps.setInt(3, g.getTeacherId()); else ps.setNull(3, Types.INTEGER);
            ps.setDouble(4, g.getMark());
            ps.setString(5, g.getTerm());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) g.setId(keys.getInt(1));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Grade> getGradesByStudent(int studentId) {
        List<Grade> list = new ArrayList<>();
        String sql = "SELECT g.*, s.name AS subject_name, t.name AS teacher_first, t.surname AS teacher_last " +
                "FROM grades g " +
                "LEFT JOIN subjects s ON g.subject_id = s.id " +
                "LEFT JOIN teachers t ON g.teacher_id = t.id " +
                "WHERE g.student_id = ? ORDER BY g.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Grade g = mapGrade(rs);
                g.setSubjectName(rs.getString("subject_name"));
                String teacherFirst = rs.getString("teacher_first");
                String teacherLast = rs.getString("teacher_last");
                if (teacherFirst != null) {
                    g.setTeacherName((teacherFirst + " " + (teacherLast != null ? teacherLast : "")).trim());
                }
                list.add(g);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Double getAverageForStudent(int studentId) {
        String sql = "SELECT AVG(mark) AS avg_mark FROM grades WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double v = rs.getDouble("avg_mark");
                if (!rs.wasNull()) return v;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<StudentPerformance> getTopStudentsByClass(String className, int limit) {
        List<StudentPerformance> list = new ArrayList<>();
        String sql = "SELECT s.id, CONCAT(s.name, ' ', s.surname) AS full_name, s.class, AVG(g.mark) AS avg_mark " +
                "FROM students s JOIN grades g ON s.id = g.student_id " +
                "WHERE s.class = ? GROUP BY s.id, s.name, s.surname, s.class " +
                "ORDER BY avg_mark DESC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, className);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            int rank = 1;
            while (rs.next()) {
                StudentPerformance perf = mapPerformance(rs, rank++);
                list.add(perf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<StudentPerformance> getTopStudentsOverall(int limit) {
        List<StudentPerformance> list = new ArrayList<>();
        String sql = "SELECT s.id, CONCAT(s.name, ' ', s.surname) AS full_name, s.class, AVG(g.mark) AS avg_mark " +
                "FROM students s JOIN grades g ON s.id = g.student_id " +
                "GROUP BY s.id, s.name, s.surname, s.class ORDER BY avg_mark DESC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            int rank = 1;
            while (rs.next()) {
                list.add(mapPerformance(rs, rank++));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private Grade mapGrade(ResultSet rs) throws SQLException {
        Grade g = new Grade();
        g.setId(rs.getInt("id"));
        g.setStudentId(rs.getInt("student_id"));
        g.setSubjectId(rs.getInt("subject_id"));
        int t = rs.getInt("teacher_id");
        if (!rs.wasNull()) g.setTeacherId(t);
        g.setMark(rs.getDouble("mark"));
        g.setTerm(rs.getString("term"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) g.setCreatedAt(ts.toLocalDateTime());
        return g;
    }

    private StudentPerformance mapPerformance(ResultSet rs, int rank) throws SQLException {
        StudentPerformance perf = new StudentPerformance();
        perf.setStudentId(rs.getInt("id"));
        perf.setFullName(rs.getString("full_name"));
        perf.setSchoolClass(rs.getString("class"));
        perf.setAverageMark(rs.getDouble("avg_mark"));
        perf.setRank(rank);
        return perf;
    }

    /**
     * Get average grade for a specific subject.
     */
    public Double getAverageForSubject(int subjectId) {
        String sql = "SELECT AVG(mark) AS avg_mark FROM grades WHERE subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subjectId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double v = rs.getDouble("avg_mark");
                if (!rs.wasNull()) return v;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get number of students enrolled in a subject (have at least one grade).
     */
    public int getStudentCountForSubject(int subjectId) {
        String sql = "SELECT COUNT(DISTINCT student_id) AS total FROM grades WHERE subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subjectId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get total number of grades recorded for a subject.
     */
    public int getGradeCountForSubject(int subjectId) {
        String sql = "SELECT COUNT(*) AS total FROM grades WHERE subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subjectId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get students enrolled in a subject with their average grade and progress.
     * Returns a list of maps containing: studentId, fullName, schoolClass, averageGrade, gradeCount
     */
    public List<java.util.Map<String, Object>> getStudentsBySubject(int subjectId) {
        List<java.util.Map<String, Object>> students = new ArrayList<>();
        String sql = "SELECT s.id AS student_id, " +
                "CONCAT(s.name, ' ', s.surname) AS full_name, " +
                "s.class AS school_class, " +
                "AVG(g.mark) AS avg_grade, " +
                "COUNT(g.id) AS grade_count " +
                "FROM students s " +
                "INNER JOIN grades g ON s.id = g.student_id " +
                "WHERE g.subject_id = ? " +
                "GROUP BY s.id, s.name, s.surname, s.class " +
                "ORDER BY avg_grade DESC, full_name ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subjectId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                java.util.Map<String, Object> student = new java.util.HashMap<>();
                student.put("studentId", rs.getInt("student_id"));
                student.put("fullName", rs.getString("full_name"));
                student.put("schoolClass", rs.getString("school_class"));
                double avg = rs.getDouble("avg_grade");
                student.put("averageGrade", rs.wasNull() ? null : avg);
                student.put("gradeCount", rs.getInt("grade_count"));
                students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }
}
