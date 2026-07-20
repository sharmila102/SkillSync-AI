package com.skillsync.controller;

import com.skillsync.dto.UserRegistrationDto;
import com.skillsync.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto registrationDto,
                                      BindingResult result,
                                      Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(registrationDto);
        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "duplicate", e.getMessage());
            return "register";
        }

        return "redirect:/login?registered";
    }
}
