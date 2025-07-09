package com.neighborfit.service;

import com.neighborfit.dto.UserRegistrationDto;
import com.neighborfit.model.User;
import com.neighborfit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for user management and business logic
 * 
 * Handles user registration, profile management, and provides
 * data access methods for the matching algorithm.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    /**
     * Register a new user
     */
    public User registerUser(UserRegistrationDto registrationDto) {
        log.info("Registering new user: {}", registrationDto.getEmail());
        
        // Check if user already exists
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new IllegalArgumentException("User with email " + registrationDto.getEmail() + " already exists");
        }
        
        // Convert DTO to entity
        User user = User.builder()
                .name(registrationDto.getName())
                .email(registrationDto.getEmail())
                .age(registrationDto.getAge())
                .gender(registrationDto.getGender())
                .maritalStatus(registrationDto.getMaritalStatus())
                .educationLevel(registrationDto.getEducationLevel())
                .incomeLevel(registrationDto.getIncomeLevel())
                .occupationType(registrationDto.getOccupationType())
                .lifestylePreferences(registrationDto.getLifestylePreferences())
                .hobbies(registrationDto.getHobbies())
                .familyStatus(registrationDto.getFamilyStatus())
                .petPreference(registrationDto.getPetPreference())
                .transportationPreference(registrationDto.getTransportationPreference())
                .preferredLocationType(registrationDto.getPreferredLocationType())
                .maxCommuteTimeMinutes(registrationDto.getMaxCommuteTimeMinutes())
                .maxDistanceMiles(registrationDto.getMaxDistanceMiles())
                .minBudget(registrationDto.getMinBudget())
                .maxBudget(registrationDto.getMaxBudget())
                .build();
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());
        return savedUser;
    }
    
    /**
     * Get user by ID
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Get user by email
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Get all users
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Update user profile
     */
    public User updateUser(Long userId, UserRegistrationDto updateDto) {
        log.info("Updating user profile for ID: {}", userId);
        
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        // Update fields
        existingUser.setName(updateDto.getName());
        existingUser.setAge(updateDto.getAge());
        existingUser.setGender(updateDto.getGender());
        existingUser.setMaritalStatus(updateDto.getMaritalStatus());
        existingUser.setEducationLevel(updateDto.getEducationLevel());
        existingUser.setIncomeLevel(updateDto.getIncomeLevel());
        existingUser.setOccupationType(updateDto.getOccupationType());
        existingUser.setLifestylePreferences(updateDto.getLifestylePreferences());
        existingUser.setHobbies(updateDto.getHobbies());
        existingUser.setFamilyStatus(updateDto.getFamilyStatus());
        existingUser.setPetPreference(updateDto.getPetPreference());
        existingUser.setTransportationPreference(updateDto.getTransportationPreference());
        existingUser.setPreferredLocationType(updateDto.getPreferredLocationType());
        existingUser.setMaxCommuteTimeMinutes(updateDto.getMaxCommuteTimeMinutes());
        existingUser.setMaxDistanceMiles(updateDto.getMaxDistanceMiles());
        existingUser.setMinBudget(updateDto.getMinBudget());
        existingUser.setMaxBudget(updateDto.getMaxBudget());
        
        User updatedUser = userRepository.save(existingUser);
        log.info("User profile updated successfully for ID: {}", userId);
        return updatedUser;
    }
    
    /**
     * Delete user
     */
    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        
        userRepository.deleteById(userId);
        log.info("User deleted successfully with ID: {}", userId);
    }
    
    /**
     * Get users by age range
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByAgeRange(Integer minAge, Integer maxAge) {
        return userRepository.findByAgeBetween(minAge, maxAge);
    }
    
    /**
     * Get users by income level
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByIncomeLevel(User.IncomeLevel incomeLevel) {
        return userRepository.findByIncomeLevel(incomeLevel);
    }
    
    /**
     * Get users by lifestyle preferences
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByLifestylePreferences(List<User.LifestylePreference> preferences) {
        return userRepository.findByLifestylePreferences(preferences);
    }
    
    /**
     * Get users by family status
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByFamilyStatus(User.FamilyStatus familyStatus) {
        return userRepository.findByFamilyStatus(familyStatus);
    }
    
    /**
     * Get users by budget range
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByBudgetRange(Integer minBudget, Integer maxBudget) {
        return userRepository.findByBudgetRange(minBudget, maxBudget);
    }
    
    /**
     * Get users by location preference
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByLocationType(User.LocationType locationType) {
        return userRepository.findByPreferredLocationType(locationType);
    }
    
    /**
     * Get users for matching based on multiple criteria
     */
    @Transactional(readOnly = true)
    public List<User> getUsersForMatching(Integer minAge, Integer maxAge, 
                                        User.IncomeLevel incomeLevel, 
                                        User.FamilyStatus familyStatus) {
        return userRepository.findUsersForMatching(minAge, maxAge, incomeLevel, familyStatus);
    }
    
    /**
     * Validate user preferences for matching
     */
    public boolean validateUserForMatching(User user) {
        if (user == null) {
            return false;
        }
        
        // Check required fields for matching
        return user.getAge() != null &&
               user.getIncomeLevel() != null &&
               user.getFamilyStatus() != null &&
               user.getMaxBudget() != null &&
               user.getMaxDistanceMiles() != null;
    }
} 