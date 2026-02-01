package model.dao;

import model.beans.Student;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ClassDAO {

    private String classColumnName;

    // returns distinct class names combining classes table + student data fallback
    public List<String> getAllClasses() {
        return combineAllClassNames();
    }

    public List<Student> getStudentsByClass(String className) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE class = ? ORDER BY surname, name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, className);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setSurname(rs.getString("surname"));
                s.setSchoolClass(rs.getString("class"));
                s.setStatus(rs.getString("status"));
                s.setRemarks(rs.getString("remarks"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addClass(String className) {
        ensureClassesTable();
        try (Connection conn = DBConnection.getConnection()) {
            String column = resolveClassColumn(conn);
            String sql = "INSERT INTO classes (" + column + ") VALUES (?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, className);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteClass(String className) {
        ensureClassesTable();
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement("UPDATE students SET class = NULL WHERE class = ?")) {
                ps.setString(1, className);
                ps.executeUpdate();
            }
            String column = resolveClassColumn(conn);
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM classes WHERE " + column + " = ?")) {
                ps.setString(1, className);
                ps.executeUpdate();
            } catch (SQLException ignored) {
                // table might not exist; ignore
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public int countClasses() {
        return combineAllClassNames().size();
    }

    public List<String> searchClasses(String keyword) {
        List<String> combined = combineAllClassNames();
        if (keyword == null || keyword.isBlank()) {
            return combined;
        }
        String lower = keyword.toLowerCase();
        List<String> filtered = new ArrayList<>();
        for (String name : combined) {
            if (name != null && name.toLowerCase().contains(lower)) {
                filtered.add(name);
            }
        }
        return filtered;
    }

    private List<String> getClassesFromStudents() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT class FROM students WHERE class IS NOT NULL AND class <> '' ORDER BY class";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(rs.getString("class"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<String> combineAllClassNames() {
        ensureClassesTable();
        Set<String> combined = new LinkedHashSet<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) {
            String column = resolveClassColumn(conn);
            String sql = "SELECT " + column + " AS class_name FROM classes ORDER BY " + column;
            try (ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    String name = rs.getString("class_name");
                    if (name != null && !name.isBlank()) {
                        combined.add(name);
                    }
                }
            }
        } catch (SQLException ignored) {
        }
        for (String fallbackName : getClassesFromStudents()) {
            if (fallbackName != null && !fallbackName.isBlank()) {
                combined.add(fallbackName);
            }
        }
        return new ArrayList<>(combined);
    }

    private void ensureClassesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS classes (" +
                "name VARCHAR(100) PRIMARY KEY" +
                ")";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException ignored) {
        }
    }

    private String resolveClassColumn(Connection conn) throws SQLException {
        if (classColumnName != null) {
            return classColumnName;
        }
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getColumns(conn.getCatalog(), null, "classes", null)) {
            while (rs.next()) {
                String column = rs.getString("COLUMN_NAME");
                if ("name".equalsIgnoreCase(column)) {
                    classColumnName = column;
                    return classColumnName;
                }
                if ("class_name".equalsIgnoreCase(column) || "classname".equalsIgnoreCase(column)) {
                    classColumnName = column;
                }
            }
        }
        if (classColumnName == null) {
            classColumnName = "name";
        }
        return classColumnName;
    }
}
