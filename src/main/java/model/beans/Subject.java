package model.beans;

/**
 * Represents a subject offered in the school.
 */
public class Subject {
    private int id;
    private String name;
    private String description;

    public Subject() {}

    public Subject(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // 🔹 Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
