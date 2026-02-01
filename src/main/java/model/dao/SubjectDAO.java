package model.dao;

import model.beans.Subject;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles CRUD for 'subjects' table.
 */
public class SubjectDAO {

    public boolean insertSubject(Subject subject) {
        String sql = "INSERT INTO subjects (name, description) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subject.getName());
            ps.setString(2, subject.getDescription());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Subject> getAllSubjects() {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM subjects";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Subject s = new Subject(rs.getString("name"), rs.getString("description"));
                s.setId(rs.getInt("id"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteSubject(int id) {
        String sql = "DELETE FROM subjects WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Subject> searchSubjects(String keyword) {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM subjects WHERE LOWER(name) LIKE ? OR LOWER(description) LIKE ? ORDER BY name";
        String term = "%" + keyword.toLowerCase() + "%";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, term);
            ps.setString(2, term);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Subject s = new Subject(rs.getString("name"), rs.getString("description"));
                s.setId(rs.getInt("id"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Subject getSubjectById(int id) {
        String sql = "SELECT * FROM subjects WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Subject s = new Subject(rs.getString("name"), rs.getString("description"));
                s.setId(rs.getInt("id"));
                return s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
