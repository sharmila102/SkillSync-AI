package com.skillsync.controller;

import com.skillsync.entity.User;
import com.skillsync.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        List<User> users = userService.getAllUsers();
        
        // Filter out admins from the list displayed for management
        List<User> regularUsers = users.stream()
                .filter(u -> "ROLE_USER".equals(u.getRole()))
                .collect(Collectors.toList());

        long totalUsers = regularUsers.size();
        long totalAdmins = users.stream().filter(u -> "ROLE_ADMIN".equals(u.getRole())).count();
        double avgMatch = userService.getAverageMatchPercentage();

        // Calculate job role distributions for reports chart
        Map<String, Long> roleDistribution = regularUsers.stream()
                .filter(u -> u.getJobRole() != null)
                .collect(Collectors.groupingBy(User::getJobRole, Collectors.counting()));

        // Default the standard 5 job roles to 0 if not present to ensure front-end chart has fields
        String[] jobRoles = {"Java Developer", "Python Developer", "Full Stack Developer", "Backend Developer", "Frontend Developer", "DevOps Engineer", "Data Scientist", "Mobile Developer"};
        for (String role : jobRoles) {
            roleDistribution.putIfAbsent(role, 0L);
        }

        model.addAttribute("users", regularUsers);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalAdmins", totalAdmins);
        model.addAttribute("avgMatchPercentage", Math.round(avgMatch * 100.0) / 100.0);
        model.addAttribute("roleDistribution", roleDistribution);

        return "admin";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        try {
            userService.deleteUser(id);
            return "redirect:/admin?success=User deleted successfully";
        } catch (Exception e) {
            return "redirect:/admin?error=Could not delete user";
        }
    }
}
