package com.skillsync.controller;

import com.skillsync.dto.UserProfileDto;
import com.skillsync.entity.User;
import com.skillsync.service.AnalysisService;
import com.skillsync.service.UserService;
import com.skillsync.util.SkillGapAnalyzer.Course;
import com.skillsync.util.SkillGapAnalyzer.Project;
import com.skillsync.util.SkillGapAnalyzer.Certification;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class DashboardController {

    private final UserService userService;
    private final AnalysisService analysisService;

    public DashboardController(UserService userService, 
                               AnalysisService analysisService) {
        this.userService = userService;
        this.analysisService = analysisService;
    }

    private User getLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database: " + email));
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User user = getLoggedInUser();
        model.addAttribute("user", user);

        // Deserialize courses, projects, certifications to display on the dashboard
        List<Course> courses = deserializeCourses(user.getCoursesJson());
        List<Project> projects = deserializeProjects(user.getProjectsJson());
        List<Certification> certifications = deserializeCertifications(user.getCertificationsJson());

        model.addAttribute("courses", courses);
        model.addAttribute("projects", projects);
        model.addAttribute("certifications", certifications);
        model.addAttribute("quizScoresMap", user.getQuizScoresMap());

        return "dashboard";
    }

    @PostMapping("/dashboard/upload-resume")
    public String uploadResume(@RequestParam("resume") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            return "redirect:/dashboard?error=Please select a file to upload.";
        }

        User user = getLoggedInUser();

        try {
            analysisService.analyzeAndSave(file.getInputStream(), file.getOriginalFilename(), user);
        } catch (IllegalArgumentException e) {
            return "redirect:/dashboard?error=" + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/dashboard?error=An error occurred while processing the file.";
        }

        return "redirect:/dashboard?success=Resume uploaded and analyzed successfully!";
    }

    @GetMapping("/dashboard/roadmap")
    public String viewRoadmap(Model model) {
        User user = getLoggedInUser();
        model.addAttribute("user", user);

        List<Course> courses = deserializeCourses(user.getCoursesJson());
        List<Project> projects = deserializeProjects(user.getProjectsJson());
        List<Certification> certifications = deserializeCertifications(user.getCertificationsJson());

        model.addAttribute("courses", courses);
        model.addAttribute("projects", projects);
        model.addAttribute("certifications", certifications);

        return "roadmap";
    }

    @GetMapping("/dashboard/profile")
    public String viewProfile(Model model) {
        User user = getLoggedInUser();
        
        UserProfileDto profileDto = new UserProfileDto();
        profileDto.setFullName(user.getFullName());
        profileDto.setJobRole(user.getJobRole());
        profileDto.setSkills(user.getSkills());

        model.addAttribute("user", user);
        model.addAttribute("profileDto", profileDto);
        return "profile";
    }

    @PostMapping("/dashboard/profile/update")
    public String updateProfile(@ModelAttribute("profileDto") @Valid UserProfileDto profileDto,
                                BindingResult result,
                                Model model) {
        User user = getLoggedInUser();

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "profile";
        }

        String oldJobRole = user.getJobRole();
        
        // Update user values
        userService.updateUserProfile(user.getEmail(), profileDto);
        
        // Fetch updated user entity
        User updatedUser = getLoggedInUser();
        
        // If the job role or manually configured skills have changed and they have resume text, recalculate analytics
        if (!profileDto.getJobRole().equalsIgnoreCase(oldJobRole) && updatedUser.getResumeText() != null) {
            analysisService.performAnalysis(updatedUser);
        }

        return "redirect:/dashboard/profile?success=Profile updated successfully!";
    }

    @PostMapping("/dashboard/roadmap/toggle")
    @org.springframework.web.bind.annotation.ResponseBody
    public java.util.Map<String, Object> toggleRoadmapItem(@RequestParam("type") String type,
                                                          @RequestParam("name") String name) {
        User user = getLoggedInUser();

        List<Course> courses = deserializeCourses(user.getCoursesJson());
        List<Project> projects = deserializeProjects(user.getProjectsJson());
        List<Certification> certifications = deserializeCertifications(user.getCertificationsJson());

        boolean updated = false;
        boolean isCompletedNow = false;

        if ("course".equalsIgnoreCase(type)) {
            for (Course c : courses) {
                if (c.getName().equalsIgnoreCase(name)) {
                    c.setCompleted(!c.isCompleted());
                    isCompletedNow = c.isCompleted();
                    updated = true;
                    break;
                }
            }
        } else if ("project".equalsIgnoreCase(type)) {
            for (Project p : projects) {
                if (p.getTitle().equalsIgnoreCase(name)) {
                    p.setCompleted(!p.isCompleted());
                    isCompletedNow = p.isCompleted();
                    updated = true;
                    break;
                }
            }
        } else if ("certification".equalsIgnoreCase(type)) {
            for (Certification c : certifications) {
                if (c.getName().equalsIgnoreCase(name)) {
                    c.setCompleted(!c.isCompleted());
                    isCompletedNow = c.isCompleted();
                    updated = true;
                    break;
                }
            }
        }

        int currentProgress = user.getProgressPercentage() != null ? user.getProgressPercentage() : 0;

        if (updated) {
            user.setCoursesJson(analysisService.serializeCourses(courses));
            user.setProjectsJson(analysisService.serializeProjects(projects));
            user.setCertificationsJson(analysisService.serializeCertifications(certifications));

            // Recalculate progressPercentage
            int totalItems = courses.size() + projects.size() + certifications.size();
            if (totalItems > 0) {
                long completedCount = courses.stream().filter(Course::isCompleted).count()
                        + projects.stream().filter(Project::isCompleted).count()
                        + certifications.stream().filter(Certification::isCompleted).count();
                
                int initialMatch = user.getMatchPercentage() != null ? user.getMatchPercentage() : 0;
                currentProgress = (int) Math.round(initialMatch + (100 - initialMatch) * ((double) completedCount / totalItems));
                user.setProgressPercentage(currentProgress);
            }
            
            userService.saveUser(user);
        }

        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("success", updated);
        response.put("completed", isCompletedNow);
        response.put("progressPercentage", currentProgress);
        return response;
    }

    // --- Custom Deserialization Helpers ( Jackson independent ) ---

    private List<Course> deserializeCourses(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        List<Course> list = new ArrayList<>();
        try {
            String[] parts = text.split("##");
            for (String part : parts) {
                String[] fields = part.split("\\|\\|");
                if (fields.length >= 4) {
                    list.add(new Course(fields[0], fields[1], fields[2], Boolean.parseBoolean(fields[3])));
                } else if (fields.length >= 3) {
                    list.add(new Course(fields[0], fields[1], fields[2], false));
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing courses serialization: " + e.getMessage());
        }
        return list;
    }

    private List<Project> deserializeProjects(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        List<Project> list = new ArrayList<>();
        try {
            String[] parts = text.split("##");
            for (String part : parts) {
                String[] fields = part.split("\\|\\|");
                if (fields.length >= 4) {
                    list.add(new Project(fields[0], fields[1], fields[2], Boolean.parseBoolean(fields[3])));
                } else if (fields.length >= 3) {
                    list.add(new Project(fields[0], fields[1], fields[2], false));
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing projects serialization: " + e.getMessage());
        }
        return list;
    }

    private List<Certification> deserializeCertifications(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        List<Certification> list = new ArrayList<>();
        try {
            String[] parts = text.split("##");
            for (String part : parts) {
                String[] fields = part.split("\\|\\|");
                if (fields.length >= 3) {
                    list.add(new Certification(fields[0], fields[1], Boolean.parseBoolean(fields[2])));
                } else if (fields.length >= 2) {
                    list.add(new Certification(fields[0], fields[1], false));
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing certifications serialization: " + e.getMessage());
        }
        return list;
    }
}
