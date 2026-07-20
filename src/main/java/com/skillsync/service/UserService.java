package com.skillsync.service;

import com.skillsync.entity.User;
import com.skillsync.dto.UserRegistrationDto;
import com.skillsync.dto.UserProfileDto;
import java.util.List;
import java.util.Optional;

public interface UserService {
    
    User registerUser(UserRegistrationDto registrationDto);
    
    Optional<User> findByEmail(String email);
    
    User updateUserProfile(String email, UserProfileDto profileDto);
    
    List<User> getAllUsers();
    
    void deleteUser(Long id);
    
    long getTotalUsersCount();
    
    long getAdminCount();
    
    double getAverageMatchPercentage();
    
    void saveUser(User user);
}
