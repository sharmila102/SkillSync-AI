package com.skillsync.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserProfileDto {

    @NotEmpty(message = "Full name cannot be empty")
    private String fullName;

    @NotEmpty(message = "Job role cannot be empty")
    private String jobRole;

    private String skills; // manual skills (comma separated list)
}
