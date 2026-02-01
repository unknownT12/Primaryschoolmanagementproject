package model.beans;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a student in the school system.
 * Includes personal, guardian, class, and academic details.
 */
public class Student {
    private int id;
    private String name;
    private String surname;
    private String birthCertificateNo;
    private String gender;
    private LocalDate dob;
    private String address;
    private String guardianName;
    private String guardianContact;
    private String guardianEmail;
    private LocalDate registrationDate;
    private String status; // Active, Inactive, Transferred, Graduated
    private String schoolClass; // e.g. "Standard 3"
    private Map<String, Double> subjectGrades = new HashMap<>(); // subject -> grade
    private String remarks;

    public Student() {}

    public Student(String name, String surname, String birthCertificateNo, String gender, LocalDate dob,
                   String address, String guardianName, String guardianContact, String guardianEmail,
                   LocalDate registrationDate, String status, String schoolClass, String remarks) {
        this.name = name;
        this.surname = surname;
        this.birthCertificateNo = birthCertificateNo;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.guardianName = guardianName;
        this.guardianContact = guardianContact;
        this.guardianEmail = guardianEmail;
        this.registrationDate = registrationDate;
        this.status = status;
        this.schoolClass = schoolClass;
        this.remarks = remarks;
    }

    // 🔹 Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getBirthCertificateNo() { return birthCertificateNo; }
    public void setBirthCertificateNo(String birthCertificateNo) { this.birthCertificateNo = birthCertificateNo; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getGuardianName() { return guardianName; }
    public void setGuardianName(String guardianName) { this.guardianName = guardianName; }
    public String getGuardianContact() { return guardianContact; }
    public void setGuardianContact(String guardianContact) { this.guardianContact = guardianContact; }
    public String getGuardianEmail() { return guardianEmail; }
    public void setGuardianEmail(String guardianEmail) { this.guardianEmail = guardianEmail; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSchoolClass() { return schoolClass; }
    public void setSchoolClass(String schoolClass) { this.schoolClass = schoolClass; }
    public Map<String, Double> getSubjectGrades() { return subjectGrades; }
    public void setSubjectGrades(Map<String, Double> subjectGrades) { this.subjectGrades = subjectGrades; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    // 🔹 Calculate average mark
    public double calculateAverageGrade() {
        if (subjectGrades.isEmpty()) return 0.0;
        return subjectGrades.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
