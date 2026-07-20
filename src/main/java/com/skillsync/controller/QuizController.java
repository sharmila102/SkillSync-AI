package com.skillsync.controller;

import com.skillsync.entity.User;
import com.skillsync.service.AnalysisService;
import com.skillsync.service.QuizService;
import com.skillsync.service.QuizService.QuizQuestion;
import com.skillsync.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class QuizController {

    private final UserService userService;
    private final QuizService quizService;
    private final AnalysisService analysisService;

    public QuizController(UserService userService,
            QuizService quizService,
            AnalysisService analysisService) {
        this.userService = userService;
        this.quizService = quizService;
        this.analysisService = analysisService;
    }

    private User getLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database: " + email));
    }

    @GetMapping("/dashboard/quiz")
    public String listQuizzes(Model model) {
        User user = getLoggedInUser();
        model.addAttribute("user", user);

        List<String> missingSkills = new ArrayList<>();
        if (user.getMissingSkills() != null && !user.getMissingSkills().trim().isEmpty()) {
            missingSkills = Arrays.stream(user.getMissingSkills().split(", "))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }

        List<String> unlockedSkills = new ArrayList<>();
        if (user.getUnlockedSkills() != null && !user.getUnlockedSkills().trim().isEmpty()) {
            unlockedSkills = Arrays.stream(user.getUnlockedSkills().split(", "))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }

        model.addAttribute("missingSkills", missingSkills);
        model.addAttribute("unlockedSkills", unlockedSkills);
        model.addAttribute("quizScores", user.getQuizScoresMap());
        model.addAttribute("quizActive", false);
        return "quiz";
    }

    @GetMapping("/dashboard/quiz/start")
    public String startQuiz(@RequestParam("skill") String skill, Model model) {
        User user = getLoggedInUser();
        model.addAttribute("user", user);

        // Check if skill is in missing skills
        boolean isMissing = false;
        if (user.getMissingSkills() != null && !user.getMissingSkills().trim().isEmpty()) {
            isMissing = Arrays.stream(user.getMissingSkills().split(", "))
                    .anyMatch(s -> s.equalsIgnoreCase(skill));
        }

        if (!isMissing) {
            return "redirect:/dashboard/quiz?error=You have already matched this skill or it is not required for your role!";
        }

        List<QuizQuestion> questions = quizService.getQuestionsForSkill(skill);
        int passingScore = (int) Math.ceil(questions.size() * 0.8);

        model.addAttribute("skill", skill);
        model.addAttribute("questions", questions);
        model.addAttribute("quizTotal", questions.size());
        model.addAttribute("passingScore", passingScore);
        model.addAttribute("quizActive", true);
        return "quiz";
    }

    @PostMapping("/dashboard/quiz/submit")
    public String submitQuiz(@RequestParam("skill") String skill,
            @RequestParam Map<String, String> allParams,
            RedirectAttributes redirectAttributes) {
        User user = getLoggedInUser();

        // Grade the quiz
        int score = quizService.gradeQuiz(skill, allParams);
        int totalQuestions = quizService.getQuestionsForSkill(skill).size();
        int passingScore = (int) Math.ceil(totalQuestions * 0.8);

        // Update user quiz scores
        Map<String, Integer> quizScores = user.getQuizScoresMap();
        quizScores.put(skill, score);
        user.setQuizScoresFromMap(quizScores);
        userService.saveUser(user);

        if (score >= passingScore) {
            String unlocked = user.getUnlockedSkills();
            if (unlocked == null || unlocked.trim().isEmpty()) {
                user.setUnlockedSkills(skill);
            } else {
                List<String> unlockedList = new ArrayList<>(Arrays.asList(unlocked.split(", ")));
                if (!unlockedList.contains(skill)) {
                    unlockedList.add(skill);
                    user.setUnlockedSkills(String.join(", ", unlockedList));
                }
            }

            // Recalculate analysis
            analysisService.performAnalysis(user);

            redirectAttributes.addAttribute("success", "Congratulations! You scored " + score + "/" + totalQuestions
                    + ", passed the quiz, and unlocked the '" + skill + "' skill!");
            return "redirect:/dashboard";
        } else {
            redirectAttributes.addAttribute("error", "You scored " + score + "/" + totalQuestions + " on the " + skill
                    + " quiz. You need at least " + passingScore + "/" + totalQuestions
                    + " correct answers to unlock the skill. Try studying the recommended resources and try again!");
            return "redirect:/dashboard/quiz";
        }
    }
}
