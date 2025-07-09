package com.neighborfit.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.NotBlank;

/**
 * Neighborhood entity representing geographic areas with demographic and lifestyle data
 * 
 * This model captures real neighborhood characteristics that will be used
 * in the matching algorithm to find the best fit for users.
 */
@Entity
@Table(name = "neighborhoods")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Neighborhood {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Neighborhood name is required")
    private String name;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "State is required")
    private String state;
    
    @NotBlank(message = "ZIP code is required")
    private String zipCode;
    
    // Geographic Information
    private Double latitude;
    private Double longitude;
    
    // Demographics
    private Integer totalPopulation;
    private Double medianAge;
    private Double medianIncome;
    private Double homeOwnershipRate;
    private Double collegeGraduateRate;
    
    // Housing Information
    private Double medianHomeValue;
    private Double medianRent;
    private Double vacancyRate;
    
    // Lifestyle Characteristics
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<LifestyleCharacteristic> lifestyleCharacteristics;
    
    // Amenities and Services
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Amenity> amenities;
    
    // Transportation
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<TransportationOption> transportationOptions;
    
    // Safety and Crime
    private Double crimeRate;
    private Double safetyScore;
    
    // Education
    private Double schoolRating;
    private Integer numberOfSchools;
    
    // Employment
    private Double unemploymentRate;
    private Double commuteTimeMinutes;
    
    // Environmental Factors
    private Double airQualityIndex;
    private Double walkScore;
    private Double bikeScore;
    private Double transitScore;
    
    // Cultural and Social
    private Double diversityIndex;
    private Integer numberOfRestaurants;
    private Integer numberOfParks;
    private Integer numberOfLibraries;
    
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
    
    // Enums for neighborhood characteristics
    public enum LifestyleCharacteristic {
        URBAN, SUBURBAN, RURAL, FAMILY_FRIENDLY, 
        YOUNG_PROFESSIONAL, RETIREMENT_COMMUNITY, 
        UNIVERSITY_TOWN, TOURIST_DESTINATION
    }
    
    public enum Amenity {
        GROCERY_STORES, RESTAURANTS, SHOPPING_CENTERS, 
        HOSPITALS, LIBRARIES, PARKS, GYMS, 
        MOVIE_THEATERS, BARS, COFFEE_SHOPS
    }
    
    public enum TransportationOption {
        BUS, TRAIN, SUBWAY, LIGHT_RAIL, 
        BIKE_LANES, WALKING_TRAILS, PARKING
    }
} 