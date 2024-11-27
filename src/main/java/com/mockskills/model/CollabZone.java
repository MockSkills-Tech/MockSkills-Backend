package com.mockskills.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "collabzone_joinus_register")
public class CollabZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "formatted_id", nullable = false, length = 10)
    @Pattern(regexp = "^[A-Za-z0-9]{1,10}$", message = "Formatted ID must contain only alphanumeric characters and be up to 10 characters long.")
    private String formattedId;

    @Column(nullable = false, length = 255, unique = true)
    @Email(message = "Email must be a valid email address.")
    @NotBlank(message = "Email is mandatory.")
    private String email;

    @Column(nullable = false, length = 255)
    @Pattern(regexp = "^[A-Za-z][A-Za-z\\s]*$", message = "Name must start with a letter and only contain letters and spaces.")
    @Size(max = 255, message = "Name must not exceed 255 characters.")
    @NotBlank(message = "Name is mandatory.")
    private String name;
    
    @Column(length = 255)
    @Pattern(
        regexp = "^[A-Za-z]+(?:[\\s-][A-Za-z]+)*(?:,\\s[A-Za-z]+(?:[\\s-][A-Za-z]+)*)?(?:,\\s[A-Za-z]+(?:[\\s-][A-Za-z]+)*)?$",
        message = "Location must follow the format 'City, Country' or 'City, State, Country'."
    )
    @Size(max = 255, message = "Location must not exceed 255 characters.")
    private String location;




    @Column(columnDefinition = "TEXT")
    @Size(max = 500, message = "Bio must not exceed 500 characters.")
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Department is mandatory.")
    private Department department;

    @Column(name = "profile_picture_url", columnDefinition = "TEXT")
    @Pattern(regexp = "^(https?://[\\w.-]+(?:\\.[\\w\\.-]+)+[/\\w\\-.?%&=]*)?$", message = "Profile picture URL must be a valid URL.")
    private String profilePictureUrl;

    @Column(name = "portfolio_url", columnDefinition = "TEXT")
    @Pattern(regexp = "^(https?://[\\w.-]+(?:\\.[\\w\\.-]+)+[/\\w\\-.?%&=]*)?$", message = "Portfolio URL must be a valid URL.")
    private String portfolioUrl;

    @Column(name = "github_url", columnDefinition = "TEXT")
    @Pattern(regexp = "^(https?://github\\.com/\\w+(/[\\w.-]*)*)?$", message = "GitHub URL must be a valid GitHub profile or repository URL.")
    private String githubUrl;

    @Column(name = "linkedin_url", columnDefinition = "TEXT")
    @Pattern(regexp = "^(https?://(www\\.)?linkedin\\.com/.*)?$", message = "LinkedIn URL must be a valid LinkedIn profile URL.")
    private String linkedinUrl;

    @Convert(converter = SkillsConverter.class)
    @NotEmpty(message = "Skills cannot be empty.")
    private List<@Pattern(regexp = "^[A-Za-z\\s\\.]+$", message = "Each skill must only contain letters, spaces, and periods.")
                  @Size(min = 1, max = 50, message = "Each skill must be between 1 and 50 characters.") String> skills;
    

    @Column(name = "join_date", nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Timestamp joinDate;

    public enum Department {
        TECH, FINANCE, MARKETING, CONSULTANCY
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormattedId() {
        return formattedId;
    }

    public void setFormattedId(String formattedId) {
        this.formattedId = formattedId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Timestamp joinDate) {
        this.joinDate = joinDate;
    }

    @Override
    public String toString() {
        return "CollabZone{" +
                "id=" + id +
                ", formattedId='" + formattedId + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", bio='" + bio + '\'' +
                ", department=" + department +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                ", portfolioUrl='" + portfolioUrl + '\'' +
                ", githubUrl='" + githubUrl + '\'' +
                ", linkedinUrl='" + linkedinUrl + '\'' +
                ", skills=" + skills +
                ", joinDate=" + joinDate +
                '}';
    }
}
