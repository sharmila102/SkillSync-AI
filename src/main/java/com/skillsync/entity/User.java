package com.skillsync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.StringJoiner;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    @Column(nullable = false)
    private String role; // "ROLE_USER" or "ROLE_ADMIN"

    @Column(name = "job_role")
    private String jobRole; // e.g. "Java Developer"

    @Column(columnDefinition = "TEXT")
    private String skills; // user inputted skills (comma separated)

    @Column(name = "resume_file_name")
    private String resumeFileName;

    @Column(name = "resume_text", columnDefinition = "LONGTEXT")
    private String resumeText;

    @Column(name = "match_percentage")
    private Integer matchPercentage;

    @Column(name = "missing_skills", columnDefinition = "TEXT")
    private String missingSkills;

    @Column(name = "unlocked_skills", columnDefinition = "TEXT")
    private String unlockedSkills;

    @Column(name = "roadmap_markdown", columnDefinition = "LONGTEXT")
    private String roadmapMarkdown;

    @Column(name = "courses_json", columnDefinition = "TEXT")
    private String coursesJson;

    @Column(name = "projects_json", columnDefinition = "TEXT")
    private String projectsJson;

    @Column(name = "certifications_json", columnDefinition = "TEXT")
    private String certificationsJson;

    @Column(name = "progress_percentage")
    private Integer progressPercentage = 0;

    @Column(name = "quiz_scores", columnDefinition = "TEXT")
    private String quizScores;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (role == null) {
            role = "ROLE_USER";
        }
        if (progressPercentage == null) {
            progressPercentage = 0;
        }
    }

    public Map<String, Integer> getQuizScoresMap() {
        if (quizScores == null || quizScores.trim().isEmpty()) {
            return new HashMap<>();
        }
        Map<String, Integer> map = new HashMap<>();
        try {
            String[] parts = quizScores.split("##");
            for (String part : parts) {
                String[] fields = part.split("\\|\\|");
                if (fields.length >= 2) {
                    map.put(fields[0], Integer.parseInt(fields[1]));
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing quiz scores: " + e.getMessage());
        }
        return map;
    }

    public void setQuizScoresFromMap(Map<String, Integer> map) {
        if (map == null || map.isEmpty()) {
            this.quizScores = "";
            return;
        }
        StringJoiner sj = new StringJoiner("##");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            sj.add(entry.getKey() + "||" + entry.getValue());
        }
        this.quizScores = sj.toString();
    }
}
