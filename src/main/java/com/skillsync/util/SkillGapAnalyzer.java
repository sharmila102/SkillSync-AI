package com.skillsync.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

@Component
public class SkillGapAnalyzer {

    // Define the required skills for each job role
    private static final Map<String, List<String>> ROLE_SKILLS = new HashMap<>();

    static {
        ROLE_SKILLS.put("Java Developer", Arrays.asList(
                "Java", "Spring Boot", "Spring MVC", "Hibernate", "JPA", "MySQL", 
                "REST API", "Maven", "Git", "Microservices", "JUnit", "Docker"
        ));
        ROLE_SKILLS.put("Python Developer", Arrays.asList(
                "Python", "Django", "Flask", "FastAPI", "MySQL", "PostgreSQL", 
                "SQL", "Git", "REST API", "NumPy", "Pandas", "Docker", "PyTest"
        ));
        ROLE_SKILLS.put("Full Stack Developer", Arrays.asList(
                "HTML5", "CSS3", "JavaScript", "React", "Angular", "Bootstrap", 
                "Java", "Spring Boot", "Node.js", "Express", "MySQL", "Git", "REST API"
        ));
        ROLE_SKILLS.put("Backend Developer", Arrays.asList(
                "Java", "Python", "Spring Boot", "Node.js", "Express", "MySQL", 
                "PostgreSQL", "REST API", "Microservices", "Docker", "Redis", "Git"
        ));
        ROLE_SKILLS.put("Frontend Developer", Arrays.asList(
                "HTML5", "CSS3", "JavaScript", "TypeScript", "React", "Angular", 
                "Vue.js", "Bootstrap", "Tailwind CSS", "Git", "Responsive Design"
        ));
        ROLE_SKILLS.put("DevOps Engineer", Arrays.asList(
                "Git", "Docker", "Kubernetes", "Jenkins", "Ansible", "Terraform", "AWS", "Linux", "Bash", "CI/CD"
        ));
        ROLE_SKILLS.put("Data Scientist", Arrays.asList(
                "Python", "SQL", "NumPy", "Pandas", "Scikit-Learn", "TensorFlow", "PyTorch", "Data Visualization", "Git"
        ));
        ROLE_SKILLS.put("Mobile Developer", Arrays.asList(
                "Java", "Kotlin", "Swift", "Flutter", "React Native", "Git", "REST API", "Mobile Design"
        ));
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Course {
        private String name;
        private String platform;
        private String duration;
        @Builder.Default
        private boolean completed = false;

        public Course(String name, String platform, String duration) {
            this.name = name;
            this.platform = platform;
            this.duration = duration;
            this.completed = false;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Project {
        private String title;
        private String description;
        private String difficulty;
        @Builder.Default
        private boolean completed = false;

        public Project(String title, String description, String difficulty) {
            this.title = title;
            this.description = description;
            this.difficulty = difficulty;
            this.completed = false;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Certification {
        private String name;
        private String authority;
        @Builder.Default
        private boolean completed = false;

        public Certification(String name, String authority) {
            this.name = name;
            this.authority = authority;
            this.completed = false;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnalysisResult {
        private int matchPercentage;
        private List<String> matchingSkills;
        private List<String> missingSkills;
        private String roadmapMarkdown;
        private List<Course> courses;
        private List<Project> projects;
        private List<Certification> certifications;
    }

    public List<String> getSkillsForRole(String role) {
        return ROLE_SKILLS.getOrDefault(role, Collections.emptyList());
    }

    public AnalysisResult analyze(String resumeText, String jobRole) {
        List<String> requiredSkills = ROLE_SKILLS.get(jobRole);
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            // Default to Java Developer if role not matching
            requiredSkills = ROLE_SKILLS.get("Java Developer");
            jobRole = "Java Developer";
        }

        List<String> matchingSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        String textToSearch = resumeText != null ? resumeText.toLowerCase() : "";

        for (String skill : requiredSkills) {
            // Check if skill is present in the text using safe case-insensitive matching
            boolean match = false;
            if (skill.equalsIgnoreCase("C++")) {
                match = textToSearch.contains("c++");
            } else if (skill.equalsIgnoreCase("C#")) {
                match = textToSearch.contains("c#");
            } else if (skill.equalsIgnoreCase("REST API") || skill.equalsIgnoreCase("REST APIs")) {
                match = textToSearch.contains("rest api") || textToSearch.contains("restful") || textToSearch.contains("web service");
            } else if (skill.equalsIgnoreCase("HTML5")) {
                match = textToSearch.contains("html5") || textToSearch.contains("html");
            } else if (skill.equalsIgnoreCase("CSS3")) {
                match = textToSearch.contains("css3") || textToSearch.contains("css");
            } else if (skill.equalsIgnoreCase("Spring MVC")) {
                match = textToSearch.contains("spring mvc") || textToSearch.contains("spring framework") || textToSearch.contains("spring mvc");
            } else {
                // Word boundary check for standard text-based skills like Java, Git, Docker, etc.
                String patternString = "\\b" + Pattern.quote(skill.toLowerCase()) + "\\b";
                Pattern pattern = Pattern.compile(patternString);
                match = pattern.matcher(textToSearch).find() || textToSearch.contains(skill.toLowerCase());
            }

            if (match) {
                matchingSkills.add(skill);
            } else {
                missingSkills.add(skill);
            }
        }

        int totalSkills = requiredSkills.size();
        int matchingCount = matchingSkills.size();
        int matchPercentage = totalSkills > 0 ? (int) Math.round(((double) matchingCount / totalSkills) * 100) : 0;

        // Generate recommendations
        List<Course> courses = new ArrayList<>();
        List<Project> projects = new ArrayList<>();
        List<Certification> certifications = new ArrayList<>();

        for (String missingSkill : missingSkills) {
            courses.add(getCourseRecommendation(missingSkill));
            projects.add(getProjectRecommendation(missingSkill));
            certifications.add(getCertificationRecommendation(missingSkill));
        }

        // If matching skills is 100% or very few missing, add generic high-level recommendations
        if (missingSkills.isEmpty()) {
            courses.add(new Course("System Design & Architecture", "Educative", "20 hours"));
            projects.add(new Project("Microservices E-Commerce Orchestration", "Design and coordinate a secure multi-service transaction pipeline using Saga pattern.", "Hard"));
            certifications.add(new Certification("AWS Certified Solutions Architect", "Amazon Web Services"));
        }

        // Generate roadmap markdown
        String roadmapMarkdown = generateRoadmap(jobRole, matchingSkills, missingSkills, matchPercentage);

        return AnalysisResult.builder()
                .matchPercentage(matchPercentage)
                .matchingSkills(matchingSkills)
                .missingSkills(missingSkills)
                .courses(courses)
                .projects(projects)
                .certifications(certifications)
                .roadmapMarkdown(roadmapMarkdown)
                .build();
    }

    private Course getCourseRecommendation(String skill) {
        return switch (skill) {
            case "Java" -> new Course("Java Programming Masterclass", "Udemy", "80 hours");
            case "Spring Boot" -> new Course("Spring Boot 3.0: Learn Spring Boot & JPA", "Udemy", "35 hours");
            case "Spring MVC" -> new Course("Spring Framework Master Class", "Udemy", "25 hours");
            case "Hibernate", "JPA" -> new Course("Hibernate & Java Persistence API (JPA)", "Pluralsight", "15 hours");
            case "MySQL", "SQL" -> new Course("The Ultimate MySQL Bootcamp", "Udemy", "20 hours");
            case "PostgreSQL" -> new Course("SQL and PostgreSQL: The Complete Developer's Guide", "Udemy", "22 hours");
            case "REST API" -> new Course("REST APIs with Java/Python", "Coursera", "12 hours");
            case "Maven" -> new Course("Apache Maven: Beginner to Guru", "Udemy", "8 hours");
            case "Git" -> new Course("Git Complete: The Definitive Guide", "Udemy", "6 hours");
            case "Microservices" -> new Course("Microservices with Spring Boot & Spring Cloud", "Udemy", "30 hours");
            case "JUnit", "PyTest" -> new Course("Java/Python Unit Testing Foundations", "Coursera", "10 hours");
            case "Docker" -> new Course("Docker & Kubernetes: The Practical Guide", "Udemy", "22 hours");
            case "Python" -> new Course("Complete Python Bootcamp From Zero to Hero", "Udemy", "24 hours");
            case "Django" -> new Course("Python Django Dev to Deployment", "Udemy", "18 hours");
            case "Flask" -> new Course("Python and Flask Boot Camp", "Udemy", "14 hours");
            case "FastAPI" -> new Course("FastAPI - The Complete Course", "Udemy", "12 hours");
            case "NumPy", "Pandas" -> new Course("Data Analysis with Python (NumPy/Pandas)", "Coursera", "15 hours");
            case "HTML5", "CSS3" -> new Course("Build Responsive Real-World Websites", "Udemy", "37 hours");
            case "JavaScript" -> new Course("The Complete JavaScript Course", "Udemy", "68 hours");
            case "TypeScript" -> new Course("Understanding TypeScript", "Udemy", "15 hours");
            case "React" -> new Course("React - The Complete Guide (incl. Hooks, Redux)", "Udemy", "48 hours");
            case "Angular" -> new Course("Angular - The Complete Guide", "Udemy", "34 hours");
            case "Vue.js" -> new Course("Vue - The Complete Guide (Router, Vuex)", "Udemy", "31 hours");
            case "Bootstrap" -> new Course("Bootstrap 5 Course - Build 3 Projects", "Udemy", "11 hours");
            case "Tailwind CSS" -> new Course("Tailwind CSS From Scratch", "Udemy", "12 hours");
            case "Node.js", "Express" -> new Course("Node.js, Express, MongoDB Bootcamp", "Udemy", "42 hours");
            case "Redis" -> new Course("Redis Bootcamp - Learn Redis From Scratch", "Udemy", "10 hours");
            case "Responsive Design" -> new Course("HTML and CSS for Responsive Web Design", "LinkedIn Learning", "8 hours");
            case "Kubernetes" -> new Course("Certified Kubernetes Administrator (CKA)", "Udemy", "22 hours");
            case "Jenkins" -> new Course("Jenkins, From Zero To Hero", "Udemy", "10 hours");
            case "Ansible" -> new Course("Ansible for the Absolute Beginner", "Udemy", "8 hours");
            case "Terraform" -> new Course("HashiCorp Certified: Terraform Associate", "Udemy", "12 hours");
            case "AWS" -> new Course("AWS Certified Solutions Architect Associate", "Udemy", "30 hours");
            case "Linux" -> new Course("Linux Administration Bootcamp", "Udemy", "18 hours");
            case "Bash" -> new Course("Bash Scripting and Shell Programming", "Udemy", "6 hours");
            case "CI/CD" -> new Course("CI/CD Pipelines with Jenkins and GitHub Actions", "Coursera", "14 hours");
            case "Scikit-Learn" -> new Course("Machine Learning with PyTorch and Scikit-Learn", "Udemy", "28 hours");
            case "TensorFlow" -> new Course("Deep Learning with TensorFlow 2.0", "Coursera", "25 hours");
            case "PyTorch" -> new Course("PyTorch for Deep Learning Bootcamp", "Udemy", "24 hours");
            case "Data Visualization" -> new Course("Data Visualization with Python and Tableau", "Coursera", "16 hours");
            case "Kotlin" -> new Course("Kotlin for Android App Development", "Udemy", "20 hours");
            case "Swift" -> new Course("iOS & Swift - The Complete iOS App Development Bootcamp", "Udemy", "55 hours");
            case "Flutter" -> new Course("Flutter & Dart - The Complete Guide", "Udemy", "42 hours");
            case "React Native" -> new Course("React Native - The Practical Guide", "Udemy", "38 hours");
            case "Mobile Design" -> new Course("Mobile App Design UI/UX with Figma", "UX Design Institute", "15 hours");
            default -> new Course("Mastering " + skill + " Development", "Coursera", "15 hours");
        };
    }

    private Project getProjectRecommendation(String skill) {
        return switch (skill) {
            case "Java" -> new Project("Inventory Management CLI Application", "Build a terminal-based system to manage stock, tracking entries and exports with Java OOP.", "Easy");
            case "Spring Boot" -> new Project("E-Commerce REST API Backend", "Create a robust backend server with database integration, security, and cart checkout endpoints.", "Medium");
            case "Spring MVC" -> new Project("Task Manager Dashboard", "Create a Thymeleaf + MVC web application to create, update, and manage daily goals.", "Medium");
            case "Hibernate", "JPA" -> new Project("Relational Student-Course Directory", "Create complex many-to-many persistence layouts with lazy loading and criteria queries.", "Medium");
            case "MySQL", "SQL", "PostgreSQL" -> new Project("Complex E-Commerce Relational Schema", "Design database schemas with custom joins, views, triggers, and indices for query optimizations.", "Medium");
            case "REST API" -> new Project("Weather Aggregator Service", "Build a controller endpoint that queries third-party weather data and parses responses cleanly.", "Easy");
            case "Maven" -> new Project("Multi-Module Maven Build Architecture", "Structure a complex project into distinct backend, core, and web packages using Maven parent pom.", "Medium");
            case "Git" -> new Project("Team Collab Simulated Repo", "Simulate rebase operations, resolve merge conflicts, and manage branch releases on GitHub.", "Easy");
            case "Microservices" -> new Project("Social Media Event-Driven Backend", "Connect different microservices (User, Post, Like) using RabbitMQ or Kafka message queues.", "Hard");
            case "JUnit", "PyTest" -> new Project("Full Coverage Library Test Suite", "Write comprehensive unit and integration tests achieving 90%+ branch coverage with Mockito.", "Medium");
            case "Docker" -> new Project("Multi-Container App Deployment", "Dockerize a Spring Boot + MySQL application and orchestrate deployment using Docker Compose.", "Medium");
            case "Python" -> new Project("Automated Web Scraper Tool", "Extract real-estate listings from online sites, formatting and cleaning outputs with Beautiful Soup.", "Easy");
            case "Django" -> new Project("Blogging Platform with Admin Console", "Create a website featuring user authentication, post creation, tags, and comment sections.", "Medium");
            case "Flask" -> new Project("URL Shortener Utility", "Build a lightweight microservice that creates shortened redirect links and tracks click statistics.", "Easy");
            case "FastAPI" -> new Project("Real-time Chat WebSocket API", "Develop a high-performance backend API supporting real-time messaging using WebSockets.", "Medium");
            case "NumPy", "Pandas" -> new Project("Stock Market Historical Analyzer", "Load, clean, and visualize financial datasets using matplotlib, computing moving averages.", "Medium");
            case "HTML5", "CSS3" -> new Project("Professional Creative Portfolio Portal", "Design a semantic web page detailing achievements using modern CSS Grid and Flexbox layouts.", "Easy");
            case "JavaScript" -> new Project("Dynamic Interactive Budget Planner", "Create a client-side dashboard tracking income/expenses with DOM edits and local storage.", "Easy");
            case "TypeScript" -> new Project("Type-Safe E-Commerce Cart Manager", "Build cart operations ensuring strictly defined interfaces for items and discounts.", "Medium");
            case "React" -> new Project("Interactive Task Kanban Board", "Build a visual drag-and-drop workspace matching Trello functionalities using React state.", "Medium");
            case "Angular" -> new Project("Enterprise Resource Planning Portal", "Create a highly structured panel with multiple lazy-loaded routes and reactive forms.", "Hard");
            case "Vue.js" -> new Project("Personal Finance Tracker Web App", "Develop a reactive single-page app displaying real-time budget balances.", "Medium");
            case "Bootstrap" -> new Project("Company Landing Page Showcase", "Assemble a responsive startup website using grids, alerts, cards, modal components.", "Easy");
            case "Tailwind CSS" -> new Project("Sleek Modern SaaS Dashboard UI", "Style a highly custom interface utilizing tailwind configuration files and utility classes.", "Medium");
            case "Node.js", "Express" -> new Project("Real-time Collaborative Whiteboard", "Develop a multi-user drawing board server synchronized instantly via Socket.io.", "Hard");
            case "Redis" -> new Project("Caching Middleware for Database Queries", "Intercept backend queries, cache results in Redis, and implement TTL expiration mechanisms.", "Hard");
            case "Responsive Design" -> new Project("Media-query Powered Interactive Blog", "Implement a CSS layout that flows from 4K desktops to mobile screens.", "Easy");
            case "Kubernetes" -> new Project("Deploying a Scalable Web App Cluster", "Create a multi-pod deployment on Kubernetes with local load balancing.", "Hard");
            case "Jenkins" -> new Project("Automated CI/CD Deployment Pipeline", "Configure a Jenkinsfile that runs checks, packages code, and deploys on commit.", "Medium");
            case "Ansible" -> new Project("Automated Server Configuration Provisioning", "Write playbooks to deploy a secure Nginx proxy and MySQL database server.", "Medium");
            case "Terraform" -> new Project("Infrastructure-as-code AWS VPC Setup", "Provision an AWS VPC containing private/public subnets and EC2 instances dynamically.", "Hard");
            case "AWS" -> new Project("Serverless Microservice API", "Assemble a CRUD API using AWS Lambda, API Gateway, and DynamoDB database.", "Medium");
            case "Linux" -> new Project("Linux System Health Monitor", "Write bash scripts to monitor cpu/ram thresholds and generate alert logs.", "Easy");
            case "Bash" -> new Project("Backup & Archiving Script", "Write a script that monitors disk usage and emails compressed backups to remote storage.", "Easy");
            case "CI/CD" -> new Project("GitHub Actions Test Integration", "Configure workflow scripts to run testing suites on branch merges.", "Easy");
            case "Scikit-Learn" -> new Project("Predictive Housing Price Model", "Train regression algorithms to predict real-estate costs based on municipal indicators.", "Medium");
            case "TensorFlow" -> new Project("Image Classification Model", "Build a convolutional neural network to categorize images from standard database sets.", "Hard");
            case "PyTorch" -> new Project("Natural Language Sentiment Classifier", "Build recurrent layers to classify review comments into positive or negative lists.", "Hard");
            case "Data Visualization" -> new Project("Interactive Pandemic Statistics Dashboard", "Construct charts displaying global infection trends dynamically with dash components.", "Medium");
            case "Kotlin" -> new Project("Android Task Organizer App", "Develop an Android utility using Room DB to schedule, create, and manage goals.", "Medium");
            case "Swift" -> new Project("iOS Personal Finance Manager", "Build a native iOS application displaying monthly budgets using SwiftUI charts.", "Medium");
            case "Flutter" -> new Project("Cross-platform E-commerce Catalog", "Build a shared-code application displaying category products using state hooks.", "Medium");
            case "React Native" -> new Project("Location-based Restaurant Finder", "Build a mobile app integrating Google Maps to show nearest restaurant locations.", "Medium");
            case "Mobile Design" -> new Project("Interactive App Mockup Design", "Design a high-fidelity clickable prototype of a travel app in Figma.", "Easy");
            default -> new Project("Custom " + skill + " System", "Design a custom application focusing on implementing code concepts of " + skill + ".", "Medium");
        };
    }



    private Certification getCertificationRecommendation(String skill) {
        return switch (skill) {
            case "Java" -> new Certification("Oracle Certified Professional: Java SE Developer", "Oracle");
            case "Spring Boot", "Spring MVC", "Hibernate", "JPA" -> new Certification("Spring Certified Professional", "VMware/Broadcom");
            case "MySQL", "SQL", "PostgreSQL" -> new Certification("Database Administration Certification", "Oracle / PostgreSQL Group");
            case "Git" -> new Certification("GitLab Certified Associate", "GitLab");
            case "Microservices", "Docker" -> new Certification("Docker Certified Associate (DCA)", "Mirantis / Docker");
            case "Python" -> new Certification("PCEP / PCAP - Certified Associate Python Programmer", "Python Institute");
            case "Django", "Flask", "FastAPI" -> new Certification("Python Web Developer Certification", "Python Institute");
            case "React", "Angular", "Vue.js", "HTML5", "CSS3", "JavaScript", "TypeScript" -> new Certification("W3C Front-End Web Developer Certification", "W3C / edX");
            case "Node.js", "Express" -> new Certification("OpenJS Node.js Application Developer (LFW211)", "Linux Foundation");
            case "Redis" -> new Certification("Redis Certified Developer", "Redis Labs");
            case "Kubernetes" -> new Certification("Certified Kubernetes Administrator (CKA)", "Cloud Native Computing Foundation");
            case "Jenkins" -> new Certification("Certified Jenkins Engineer (CJE)", "CloudBees");
            case "Terraform" -> new Certification("HashiCorp Certified: Terraform Associate", "HashiCorp");
            case "AWS" -> new Certification("AWS Certified Solutions Architect - Associate", "Amazon Web Services");
            case "Linux" -> new Certification("Red Hat Certified System Administrator (RHCSA)", "Red Hat");
            case "CI/CD" -> new Certification("DevOps Institute CI/CD Architect", "DevOps Institute");
            case "Scikit-Learn", "TensorFlow", "PyTorch" -> new Certification("TensorFlow Developer Certificate", "Google");
            case "Data Visualization" -> new Certification("Tableau Desktop Specialist", "Tableau");
            case "Kotlin", "Mobile Design" -> new Certification("Associate Android Developer", "Google");
            case "Swift" -> new Certification("App Development with Swift Certified User", "Apple / Certiport");
            case "Flutter", "React Native" -> new Certification("Certified Mobile App Developer", "Industry Standards");
            default -> new Certification("Certified " + skill + " Professional", "Industry Standards");
        };
    }

    private String generateRoadmap(String role, List<String> matching, List<String> missing, int matchPercentage) {
        StringBuilder sb = new StringBuilder();
        sb.append("# AI Skill Learning Roadmap: ").append(role).append("\n\n");
        sb.append("This personalized learning roadmap has been generated dynamically based on your resume analysis. Your current skill match is **").append(matchPercentage).append("%**.\n\n");

        if (missing.isEmpty()) {
            sb.append("### 🎉 Congratulations! You possess all required skills for a **").append(role).append("**!\n\n");
            sb.append("Here are some recommended paths for continuous growth and architectural excellence:\n");
            sb.append("- **Master System Architecture**: Learn about high scale distributed transactions, horizontal scaling, caching strategies, and load balancing.\n");
            sb.append("- **Expand Cloud Proficiency**: Certify in AWS Solutions Architect, Azure Developer, or Google Cloud Engineer.\n");
            sb.append("- **Open Source Contribution**: Contribute to popular repositories in your tech stack to gain deep production knowledge.\n");
            return sb.toString();
        }

        sb.append("## 🚀 Your Skills Roadmap\n\n");

        // Group missing skills into 3 phases: Core, Frameworks/Databases, Tools/DevOps
        List<String> coreSkills = new ArrayList<>();
        List<String> frameworkDbSkills = new ArrayList<>();
        List<String> toolsDevOpsSkills = new ArrayList<>();

        for (String skill : missing) {
            String lower = skill.toLowerCase();
            if (lower.equals("java") || lower.equals("python") || lower.equals("javascript") || lower.equals("typescript") || lower.equals("html5") || lower.equals("css3") || lower.equals("kotlin") || lower.equals("swift")) {
                coreSkills.add(skill);
            } else if (lower.contains("git") || lower.contains("maven") || lower.contains("docker") || lower.contains("pytest") || lower.contains("junit") || lower.contains("responsive") || lower.contains("kubernetes") || lower.contains("jenkins") || lower.contains("ansible") || lower.contains("terraform") || lower.contains("aws") || lower.contains("linux") || lower.contains("bash") || lower.contains("ci/cd")) {
                toolsDevOpsSkills.add(skill);
            } else {
                frameworkDbSkills.add(skill);
            }
        }

        int phase = 1;

        if (!coreSkills.isEmpty()) {
            sb.append("### Phase ").append(phase++).append(": Strengthen Programming Foundations (Weeks 1-3)\n");
            sb.append("Focus on core language syntaxes, data structures, and algorithms. Make sure to complete coding assignments and small CLI utilities.\n\n");
            for (String skill : coreSkills) {
                sb.append("- **").append(skill).append("**: ").append("Learn variables, OOP rules, error handling, and asynchronous operations. Recommended: *").append(getCourseRecommendation(skill).getName()).append("*.\n");
            }
            sb.append("\n");
        }

        if (!frameworkDbSkills.isEmpty()) {
            sb.append("### Phase ").append(phase++).append(": Master Core Frameworks & Storage (Weeks 4-8)\n");
            sb.append("Learn standard enterprise architectures, MVC routing, data queries, ORM persistence, and API designs.\n\n");
            for (String skill : frameworkDbSkills) {
                sb.append("- **").append(skill).append("**: ").append("Build database interfaces, REST request controllers, and integrate security layers. Recommended: *").append(getCourseRecommendation(skill).getName()).append("*.\n");
            }
            sb.append("\n");
        }

        if (!toolsDevOpsSkills.isEmpty()) {
            sb.append("### Phase ").append(phase).append(": Build, Containerize & Test (Weeks 9-11)\n");
            sb.append("Add build automations, unit testing strategies, branch management skills, and container deployment capabilities.\n\n");
            for (String skill : toolsDevOpsSkills) {
                sb.append("- **").append(skill).append("**: ").append("Write mock assertions, automate jar builds, configure deployment containers. Recommended: *").append(getCourseRecommendation(skill).getName()).append("*.\n");
            }
            sb.append("\n");
        }

        sb.append("## 🏆 Project Milestones\n\n");
        sb.append("To validate your learning, we suggest building the following project pipeline in order:\n\n");
        int index = 1;
        for (String skill : missing) {
            Project p = getProjectRecommendation(skill);
            sb.append("").append(index++).append(". **").append(p.getTitle()).append("** (*Difficulty: ").append(p.getDifficulty()).append("*)\n");
            sb.append("   - *Skill Goal*: ").append(skill).append("\n");
            sb.append("   - *Task*: ").append(p.getDescription()).append("\n\n");
            if (index > 4) break; // limit to 4 projects maximum
        }

        return sb.toString();
    }
}
