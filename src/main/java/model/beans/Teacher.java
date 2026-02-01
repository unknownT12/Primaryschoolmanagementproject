package model.beans;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a teacher in the system with their details,
 * subjects they teach, and classes they handle.
 */
public class Teacher {
    private int id;
    private String name;
    private String surname;
    private String omangOrPassport;
    private String gender;
    private LocalDate dob;
    private String address;
    private String contactNo;
    private String email;
    private String qualifications;
    private List<String> subjectsQualified; // subjects teacher can teach
    private Map<String, String> classToSubject = new HashMap<>(); // class -> subject mapping
    private LocalDate dateJoined;

    public Teacher() {}

    public Teacher(String name, String surname, String omangOrPassport, String gender, LocalDate dob,
                   String address, String contactNo, String email, String qualifications,
                   List<String> subjectsQualified, LocalDate dateJoined) {
        this.name = name;
        this.surname = surname;
        this.omangOrPassport = omangOrPassport;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.contactNo = contactNo;
        this.email = email;
        this.qualifications = qualifications;
        this.subjectsQualified = subjectsQualified;
        this.dateJoined = dateJoined;
    }

    // 🔹 Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getOmangOrPassport() { return omangOrPassport; }
    public void setOmangOrPassport(String omangOrPassport) { this.omangOrPassport = omangOrPassport; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getQualifications() { return qualifications; }
    public void setQualifications(String qualifications) { this.qualifications = qualifications; }
    public List<String> getSubjectsQualified() { return subjectsQualified; }
    public void setSubjectsQualified(List<String> subjectsQualified) { this.subjectsQualified = subjectsQualified; }
    public Map<String, String> getClassToSubject() { return classToSubject; }
    public void setClassToSubject(Map<String, String> classToSubject) { this.classToSubject = classToSubject; }
    public LocalDate getDateJoined() { return dateJoined; }
    public void setDateJoined(LocalDate dateJoined) { this.dateJoined = dateJoined; }
}
