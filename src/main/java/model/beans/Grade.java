package model.beans;

import java.time.LocalDateTime;

public class Grade {
    private int id;
    private int studentId;
    private int subjectId;
    private Integer teacherId;
    private Double mark;
    private String term;
    private LocalDateTime createdAt;
    private String subjectName;
    private String teacherName;

    public Grade() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }
    public Integer getTeacherId() { return teacherId; }
    public void setTeacherId(Integer teacherId) { this.teacherId = teacherId; }
    public Double getMark() { return mark; }
    public void setMark(Double mark) { this.mark = mark; }
    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
}
