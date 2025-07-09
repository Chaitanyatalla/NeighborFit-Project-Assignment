package com.neighborfit.dto;

import com.neighborfit.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.*;
import java.util.List;

/**
 * Data Transfer Object for user registration
 * 
 * This DTO handles the initial user registration process and
 * captures all necessary information for the matching algorithm.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationDto {
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 120, message = "Age must be less than 120")
    private Integer age;
    
    private User.Gender gender;
    private User.MaritalStatus maritalStatus;
    private User.EducationLevel educationLevel;
    private User.IncomeLevel incomeLevel;
    private User.OccupationType occupationType;
    
    // Lifestyle Preferences
    private List<User.LifestylePreference> lifestylePreferences;
    private List<User.Hobby> hobbies;
    private User.FamilyStatus familyStatus;
    private User.PetPreference petPreference;
    private User.TransportationPreference transportationPreference;
    
    // Location Preferences
    private User.LocationType preferredLocationType;
    
    @Min(value = 0, message = "Max commute time must be positive")
    @Max(value = 120, message = "Max commute time must be reasonable")
    private Integer maxCommuteTimeMinutes;
    
    @Min(value = 0, message = "Max distance must be positive")
    @Max(value = 100, message = "Max distance must be reasonable")
    private Integer maxDistanceMiles;
    
    // Budget Preferences
    @Min(value = 0, message = "Min budget must be positive")
    private Integer minBudget;
    
    @Min(value = 0, message = "Max budget must be positive")
    private Integer maxBudget;
} 