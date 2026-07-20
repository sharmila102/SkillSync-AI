package com.skillsync.service;

import com.skillsync.entity.User;
import com.skillsync.repository.UserRepository;
import com.skillsync.util.ResumeParser;
import com.skillsync.util.SkillGapAnalyzer;
import com.skillsync.util.SkillGapAnalyzer.AnalysisResult;
import com.skillsync.util.SkillGapAnalyzer.Course;
import com.skillsync.util.SkillGapAnalyzer.Project;
import com.skillsync.util.SkillGapAnalyzer.Certification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.StringJoiner;

@Service
@Transactional
public class AnalysisService {

    private final ResumeParser resumeParser;
    private final SkillGapAnalyzer skillGapAnalyzer;
    private final UserRepository userRepository;

    public AnalysisService(ResumeParser resumeParser, 
                           SkillGapAnalyzer skillGapAnalyzer, 
                           UserRepository userRepository) {
        this.resumeParser = resumeParser;
        this.skillGapAnalyzer = skillGapAnalyzer;
        this.userRepository = userRepository;
    }

    public void analyzeAndSave(InputStream fileStream, String fileName, User user) throws IOException {
        // 1. Extract text from resume
        String extractedText = resumeParser.parse(fileStream, fileName);
        user.setResumeFileName(fileName);
        user.setResumeText(extractedText);

        // 2. Perform AI skill gap analysis
        performAnalysis(user);
    }

    public void performAnalysis(User user) {
        if (user.getResumeText() == null || user.getResumeText().isEmpty()) {
            return;
        }

        // Run gap analyzer on user's target job role, combining extracted resume text and unlocked skills
        String analysisText = user.getResumeText();
        if (user.getUnlockedSkills() != null && !user.getUnlockedSkills().trim().isEmpty()) {
            analysisText += "\nUnlocked Skills: " + user.getUnlockedSkills();
        }

        AnalysisResult result = skillGapAnalyzer.analyze(analysisText, user.getJobRole());

        // Update user entity analytical fields
        user.setMatchPercentage(result.getMatchPercentage());
        user.setProgressPercentage(result.getMatchPercentage()); // Use match percentage as skill progress initially

        // Store matching skills as a comma-separated list in user's profile
        StringJoiner joiner = new StringJoiner(", ");
        for (String skill : result.getMatchingSkills()) {
            joiner.add(skill);
        }
        user.setSkills(joiner.toString());

        // Store missing skills
        StringJoiner missingJoiner = new StringJoiner(", ");
        for (String skill : result.getMissingSkills()) {
            missingJoiner.add(skill);
        }
        user.setMissingSkills(missingJoiner.toString());

        // Store roadmap markdown
        user.setRoadmapMarkdown(result.getRoadmapMarkdown());

        // Serialize recommendations using a custom text-based delimiter format (robust & database-agnostic)
        user.setCoursesJson(serializeCourses(result.getCourses()));
        user.setProjectsJson(serializeProjects(result.getProjects()));
        user.setCertificationsJson(serializeCertifications(result.getCertifications()));

        userRepository.save(user);
    }

    // Helper to serialize courses: Name||Platform||Duration||Completed##Name||Platform||Duration||Completed
    public String serializeCourses(List<Course> list) {
        if (list == null || list.isEmpty()) return "";
        StringJoiner sj = new StringJoiner("##");
        for (Course c : list) {
            sj.add(c.getName() + "||" + c.getPlatform() + "||" + c.getDuration() + "||" + c.isCompleted());
        }
        return sj.toString();
    }

    // Helper to serialize projects: Title||Description||Difficulty||Completed##Title||Description||Difficulty||Completed
    public String serializeProjects(List<Project> list) {
        if (list == null || list.isEmpty()) return "";
        StringJoiner sj = new StringJoiner("##");
        for (Project p : list) {
            sj.add(p.getTitle() + "||" + p.getDescription() + "||" + p.getDifficulty() + "||" + p.isCompleted());
        }
        return sj.toString();
    }

    // Helper to serialize certifications: Name||Authority||Completed##Name||Authority||Completed
    public String serializeCertifications(List<Certification> list) {
        if (list == null || list.isEmpty()) return "";
        StringJoiner sj = new StringJoiner("##");
        for (Certification c : list) {
            sj.add(c.getName() + "||" + c.getAuthority() + "||" + c.isCompleted());
        }
        return sj.toString();
    }
}
