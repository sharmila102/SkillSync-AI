package com.skillsync.config;

import com.skillsync.entity.User;
import com.skillsync.repository.UserRepository;
import com.skillsync.service.AnalysisService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AnalysisService analysisService;

    public DataInitializer(UserRepository userRepository, 
                           PasswordEncoder passwordEncoder,
                           AnalysisService analysisService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.analysisService = analysisService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed Admin Account
        if (!userRepository.existsByEmail("admin@skillsync.com")) {
            User admin = User.builder()
                    .fullName("SkillSync Admin")
                    .email("admin@skillsync.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ROLE_ADMIN")
                    .jobRole("System Administrator")
                    .progressPercentage(100)
                    .build();
            userRepository.save(admin);
            System.out.println("Admin account seeded successfully: admin@skillsync.com / admin123");
        }

        // Seed Sample User Account
        if (!userRepository.existsByEmail("user@skillsync.com")) {
            User user = User.builder()
                    .fullName("Praveen Kumar")
                    .email("user@skillsync.com")
                    .password(passwordEncoder.encode("user123"))
                    .role("ROLE_USER")
                    .jobRole("Java Developer")
                    .resumeFileName("praveen_resume.pdf")
                    .resumeText("Praveen Kumar - Software Engineer\n" +
                            "Skills: Java, Git, MySQL, REST API, HTML5, CSS3, JavaScript.\n" +
                            "Looking for Java Developer roles. Familiar with spring basics.")
                    .progressPercentage(0)
                    .build();
            
            userRepository.save(user);
            
            // Generate analysis results immediately for the seeded user so the dashboard has rich preview data
            analysisService.performAnalysis(user);
            System.out.println("Sample user account seeded and analyzed successfully: user@skillsync.com / user123");
        }
    }
}
