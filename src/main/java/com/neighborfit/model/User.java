package com.neighborfit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User entity representing individuals seeking neighborhood matches
 * 
 * This model captures user preferences, demographics, and lifestyle choices
 * that will be used in the matching algorithm.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;
    
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 120, message = "Age must be less than 120")
    private Integer age;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
    
    @Enumerated(EnumType.STRING)
    private EducationLevel educationLevel;
    
    @Enumerated(EnumType.STRING)
    private IncomeLevel incomeLevel;
    
    @Enumerated(EnumType.STRING)
    private OccupationType occupationType;
    
    // Lifestyle Preferences
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<LifestylePreference> lifestylePreferences;
    
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Hobby> hobbies;
    
    @Enumerated(EnumType.STRING)
    private FamilyStatus familyStatus;
    
    @Enumerated(EnumType.STRING)
    private PetPreference petPreference;
    
    @Enumerated(EnumType.STRING)
    private TransportationPreference transportationPreference;
    
    // Location Preferences
    @Enumerated(EnumType.STRING)
    private LocationType preferredLocationType;
    
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
    
    // Timestamps
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Enums for various attributes
    public enum Gender { MALE, FEMALE, OTHER, PREFER_NOT_TO_SAY }
    
    public enum MaritalStatus { SINGLE, MARRIED, DIVORCED, WIDOWED, PARTNERED }
    
    public enum EducationLevel { HIGH_SCHOOL, BACHELORS, MASTERS, PHD, OTHER }
    
    public enum IncomeLevel { LOW, MEDIUM, HIGH, VERY_HIGH }
    
    public enum OccupationType { 
        TECHNOLOGY, HEALTHCARE, EDUCATION, FINANCE, 
        MANUFACTURING, RETAIL, GOVERNMENT, OTHER 
    }
    
    public enum LifestylePreference {
        URBAN, SUBURBAN, RURAL, ACTIVE, QUIET, 
        FAMILY_ORIENTED, YOUNG_PROFESSIONAL, RETIREMENT
    }
    
    public enum Hobby {
        SPORTS, READING, COOKING, TRAVEL, MUSIC, 
        ART, GARDENING, GAMING, FITNESS, PHOTOGRAPHY
    }
    
    public enum FamilyStatus { SINGLE, COUPLE, WITH_CHILDREN, EMPTY_NESTER }
    
    public enum PetPreference { DOGS, CATS, NO_PETS, ANY_PETS }
    
    public enum TransportationPreference { CAR, PUBLIC_TRANSIT, WALKING, BIKING }
    
    public enum LocationType { CITY_CENTER, SUBURB, RURAL, UNIVERSITY_AREA }
} 