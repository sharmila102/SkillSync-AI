package com.skillsync.service;

import com.skillsync.dto.UserProfileDto;
import com.skillsync.dto.UserRegistrationDto;
import com.skillsync.entity.User;
import com.skillsync.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new IllegalArgumentException("An account already exists with email: " + registrationDto.getEmail());
        }

        User user = User.builder()
                .fullName(registrationDto.getFullName())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .jobRole(registrationDto.getJobRole())
                .role("ROLE_USER")
                .progressPercentage(0)
                .build();

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User updateUserProfile(String email, UserProfileDto profileDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        user.setFullName(profileDto.getFullName());
        user.setJobRole(profileDto.getJobRole());
        user.setSkills(profileDto.getSkills());

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public long getTotalUsersCount() {
        return userRepository.countByRole("ROLE_USER");
    }

    @Override
    public long getAdminCount() {
        return userRepository.countByRole("ROLE_ADMIN");
    }

    @Override
    public double getAverageMatchPercentage() {
        List<User> users = userRepository.findByRole("ROLE_USER");
        if (users.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        int count = 0;
        for (User u : users) {
            if (u.getMatchPercentage() != null) {
                sum += u.getMatchPercentage();
                count++;
            }
        }
        return count > 0 ? (sum / count) : 0.0;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
