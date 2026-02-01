package model.beans;

import java.util.List;

/**
 * Represents a class level in the school (e.g. Standard 1, Standard 2).
 */
public class ClassRoom {
    private int id;
    private String className;   // e.g., "Standard 3"
    private Teacher classTeacher;
    private List<Student> students;

    public ClassRoom() {}

    public ClassRoom(String className, Teacher classTeacher, List<Student> students) {
        this.className = className;
        this.classTeacher = classTeacher;
        this.students = students;
    }

    // 🔹 Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public Teacher getClassTeacher() { return classTeacher; }
    public void setClassTeacher(Teacher classTeacher) { this.classTeacher = classTeacher; }
    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }

    // 🔹 Calculate top 5 students (simple demo)
    public List<Student> getTopStudents() {
        return students.stream()
                .sorted((a, b) -> Double.compare(b.calculateAverageGrade(), a.calculateAverageGrade()))
                .limit(5)
                .toList();
    }
}

