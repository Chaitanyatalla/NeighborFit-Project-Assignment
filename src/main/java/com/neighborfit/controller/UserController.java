package com.neighborfit.controller;

import com.neighborfit.dto.UserRegistrationDto;
import com.neighborfit.model.User;
import com.neighborfit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST API controller for user management
 * 
 * Provides endpoints for user registration, profile management,
 * and user data retrieval for the matching system.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    /**
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            log.info("Received user registration request for email: {}", registrationDto.getEmail());
            User registeredUser = userService.registerUser(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (IllegalArgumentException e) {
            log.error("User registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error during user registration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get user by ID
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        try {
            log.info("Getting user by ID: {}", userId);
            Optional<User> user = userService.getUserById(userId);
            return user.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error getting user by ID {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get user by email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        try {
            log.info("Getting user by email: {}", email);
            Optional<User> user = userService.getUserByEmail(email);
            return user.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error getting user by email {}: {}", email, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get all users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            log.info("Getting all users");
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error getting all users: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Update user profile
     */
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, 
                                         @Valid @RequestBody UserRegistrationDto updateDto) {
        try {
            log.info("Updating user profile for ID: {}", userId);
            User updatedUser = userService.updateUser(userId, updateDto);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            log.error("User update failed: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error during user update: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Delete user
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        try {
            log.info("Deleting user with ID: {}", userId);
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("User deletion failed: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error during user deletion: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get users by age range
     */
    @GetMapping("/age-range")
    public ResponseEntity<List<User>> getUsersByAgeRange(@RequestParam Integer minAge, 
                                                        @RequestParam Integer maxAge) {
        try {
            log.info("Getting users by age range: {} - {}", minAge, maxAge);
            List<User> users = userService.getUsersByAgeRange(minAge, maxAge);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error getting users by age range: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get users by income level
     */
    @GetMapping("/income-level/{incomeLevel}")
    public ResponseEntity<List<User>> getUsersByIncomeLevel(@PathVariable User.IncomeLevel incomeLevel) {
        try {
            log.info("Getting users by income level: {}", incomeLevel);
            List<User> users = userService.getUsersByIncomeLevel(incomeLevel);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error getting users by income level: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get users by family status
     */
    @GetMapping("/family-status/{familyStatus}")
    public ResponseEntity<List<User>> getUsersByFamilyStatus(@PathVariable User.FamilyStatus familyStatus) {
        try {
            log.info("Getting users by family status: {}", familyStatus);
            List<User> users = userService.getUsersByFamilyStatus(familyStatus);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error getting users by family status: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get users by location type preference
     */
    @GetMapping("/location-type/{locationType}")
    public ResponseEntity<List<User>> getUsersByLocationType(@PathVariable User.LocationType locationType) {
        try {
            log.info("Getting users by location type: {}", locationType);
            List<User> users = userService.getUsersByLocationType(locationType);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error getting users by location type: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Validate user for matching
     */
    @GetMapping("/{userId}/validate-matching")
    public ResponseEntity<Boolean> validateUserForMatching(@PathVariable Long userId) {
        try {
            log.info("Validating user for matching: {}", userId);
            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {
                boolean isValid = userService.validateUserForMatching(user.get());
                return ResponseEntity.ok(isValid);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error validating user for matching: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 