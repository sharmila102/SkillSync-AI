package com.skillsync.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {

    @NotEmpty(message = "Full Name is required")
    private String fullName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotEmpty(message = "Job Role is required")
    private String jobRole;
}
