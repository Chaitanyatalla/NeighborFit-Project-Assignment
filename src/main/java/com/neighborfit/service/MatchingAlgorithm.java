package com.neighborfit.service;

import com.neighborfit.model.Match;
import com.neighborfit.model.Neighborhood;
import com.neighborfit.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Core matching algorithm service for NeighborFit
 * 
 * This service implements the neighborhood-lifestyle matching algorithm
 * that analyzes user preferences and neighborhood characteristics to
 * find optimal matches based on multiple weighted criteria.
 * 
 * Algorithm Design:
 * - Lifestyle Compatibility (40%): Hobbies, family status, pet preferences
 * - Demographic Alignment (30%): Age, income, education, occupation
 * - Location & Transportation (30%): Commute time, walkability, transit
 * 
 * The algorithm uses weighted scoring with normalization to ensure
 * fair comparison across different criteria.
 */
@Service
@Slf4j
public class MatchingAlgorithm {
    
    @Value("${app.matching.algorithm.weight.lifestyle:0.4}")
    private double lifestyleWeight;
    
    @Value("${app.matching.algorithm.weight.demographics:0.3}")
    private double demographicsWeight;
    
    @Value("${app.matching.algorithm.weight.location:0.3}")
    private double locationWeight;
    
    @Value("${app.matching.algorithm.max-distance-miles:50}")
    private int maxDistanceMiles;
    
    /**
     * Calculate match score between user and neighborhood
     */
    public Match calculateMatch(User user, Neighborhood neighborhood) {
        log.info("Calculating match for user {} and neighborhood {}", user.getName(), neighborhood.getName());
        
        // Calculate component scores
        double lifestyleScore = calculateLifestyleScore(user, neighborhood);
        double demographicScore = calculateDemographicScore(user, neighborhood);
        double locationScore = calculateLocationScore(user, neighborhood);
        double budgetScore = calculateBudgetScore(user, neighborhood);
        double amenityScore = calculateAmenityScore(user, neighborhood);
        
        // Calculate weighted overall score
        double overallScore = (lifestyleScore * lifestyleWeight) +
                            (demographicScore * demographicsWeight) +
                            (locationScore * locationWeight);
        
        // Normalize to 0-100 scale
        overallScore = Math.min(100.0, Math.max(0.0, overallScore * 100));
        
        // Determine match strength
        Match.MatchStrength matchStrength = Match.MatchStrength.fromScore(overallScore);
        
        // Generate reasoning and recommendations
        String reasoning = generateMatchReasoning(user, neighborhood, lifestyleScore, demographicScore, locationScore);
        String recommendations = generateRecommendations(user, neighborhood, overallScore);
        
        return Match.builder()
                .user(user)
                .neighborhood(neighborhood)
                .overallScore(overallScore)
                .lifestyleScore(lifestyleScore * 100)
                .demographicScore(demographicScore * 100)
                .locationScore(locationScore * 100)
                .budgetScore(budgetScore * 100)
                .amenityScore(amenityScore * 100)
                .matchStrength(matchStrength)
                .matchReasoning(reasoning)
                .recommendations(recommendations)
                .build();
    }
    
    /**
     * Calculate lifestyle compatibility score (0-1)
     */
    private double calculateLifestyleScore(User user, Neighborhood neighborhood) {
        double score = 0.0;
        int factors = 0;
        
        // Family status compatibility
        if (user.getFamilyStatus() != null && neighborhood.getLifestyleCharacteristics() != null) {
            if (user.getFamilyStatus() == User.FamilyStatus.WITH_CHILDREN && 
                neighborhood.getLifestyleCharacteristics().contains(Neighborhood.LifestyleCharacteristic.FAMILY_FRIENDLY)) {
                score += 1.0;
            } else if (user.getFamilyStatus() == User.FamilyStatus.SINGLE && 
                       neighborhood.getLifestyleCharacteristics().contains(Neighborhood.LifestyleCharacteristic.YOUNG_PROFESSIONAL)) {
                score += 1.0;
            }
            factors++;
        }
        
        // Pet preference compatibility
        if (user.getPetPreference() != null) {
            if (user.getPetPreference() == User.PetPreference.DOGS && 
                neighborhood.getAmenities() != null && 
                neighborhood.getAmenities().contains(Neighborhood.Amenity.PARKS)) {
                score += 0.8;
            }
            factors++;
        }
        
        // Transportation preference compatibility
        if (user.getTransportationPreference() != null && neighborhood.getTransportationOptions() != null) {
            switch (user.getTransportationPreference()) {
                case PUBLIC_TRANSIT:
                    if (neighborhood.getTransitScore() != null && neighborhood.getTransitScore() > 70) {
                        score += 1.0;
                    }
                    break;
                case WALKING:
                    if (neighborhood.getWalkScore() != null && neighborhood.getWalkScore() > 80) {
                        score += 1.0;
                    }
                    break;
                case BIKING:
                    if (neighborhood.getBikeScore() != null && neighborhood.getBikeScore() > 70) {
                        score += 1.0;
                    }
                    break;
            }
            factors++;
        }
        
        return factors > 0 ? score / factors : 0.0;
    }
    
    /**
     * Calculate demographic alignment score (0-1)
     */
    private double calculateDemographicScore(User user, Neighborhood neighborhood) {
        double score = 0.0;
        int factors = 0;
        
        // Age compatibility
        if (user.getAge() != null && neighborhood.getMedianAge() != null) {
            double ageDifference = Math.abs(user.getAge() - neighborhood.getMedianAge());
            if (ageDifference <= 5) {
                score += 1.0;
            } else if (ageDifference <= 10) {
                score += 0.8;
            } else if (ageDifference <= 15) {
                score += 0.6;
            } else {
                score += 0.4;
            }
            factors++;
        }
        
        // Income compatibility
        if (user.getIncomeLevel() != null && neighborhood.getMedianIncome() != null) {
            double incomeCompatibility = calculateIncomeCompatibility(user.getIncomeLevel(), neighborhood.getMedianIncome());
            score += incomeCompatibility;
            factors++;
        }
        
        // Education compatibility
        if (user.getEducationLevel() != null && neighborhood.getCollegeGraduateRate() != null) {
            double educationCompatibility = calculateEducationCompatibility(user.getEducationLevel(), neighborhood.getCollegeGraduateRate());
            score += educationCompatibility;
            factors++;
        }
        
        return factors > 0 ? score / factors : 0.0;
    }
    
    /**
     * Calculate location and transportation score (0-1)
     */
    private double calculateLocationScore(User user, Neighborhood neighborhood) {
        double score = 0.0;
        int factors = 0;
        
        // Location type preference
        if (user.getPreferredLocationType() != null && neighborhood.getLifestyleCharacteristics() != null) {
            double locationTypeScore = calculateLocationTypeCompatibility(user.getPreferredLocationType(), neighborhood.getLifestyleCharacteristics());
            score += locationTypeScore;
            factors++;
        }
        
        // Commute time compatibility
        if (user.getMaxCommuteTimeMinutes() != null && neighborhood.getCommuteTimeMinutes() != null) {
            if (neighborhood.getCommuteTimeMinutes() <= user.getMaxCommuteTimeMinutes()) {
                score += 1.0;
            } else {
                score += Math.max(0.0, 1.0 - (neighborhood.getCommuteTimeMinutes() - user.getMaxCommuteTimeMinutes()) / 60.0);
            }
            factors++;
        }
        
        // Walkability score
        if (neighborhood.getWalkScore() != null) {
            score += neighborhood.getWalkScore() / 100.0;
            factors++;
        }
        
        // Transit score
        if (neighborhood.getTransitScore() != null) {
            score += neighborhood.getTransitScore() / 100.0;
            factors++;
        }
        
        return factors > 0 ? score / factors : 0.0;
    }
    
    /**
     * Calculate budget compatibility score (0-1)
     */
    private double calculateBudgetScore(User user, Neighborhood neighborhood) {
        if (user.getMaxBudget() == null || neighborhood.getMedianHomeValue() == null) {
            return 0.5; // Neutral score if data unavailable
        }
        
        double budgetRatio = neighborhood.getMedianHomeValue() / user.getMaxBudget();
        
        if (budgetRatio <= 0.8) {
            return 1.0; // Well within budget
        } else if (budgetRatio <= 1.0) {
            return 0.8; // Within budget
        } else if (budgetRatio <= 1.2) {
            return 0.6; // Slightly over budget
        } else {
            return 0.2; // Significantly over budget
        }
    }
    
    /**
     * Calculate amenity compatibility score (0-1)
     */
    private double calculateAmenityScore(User user, Neighborhood neighborhood) {
        if (neighborhood.getAmenities() == null || neighborhood.getAmenities().isEmpty()) {
            return 0.5; // Neutral score if no amenity data
        }
        
        double score = 0.0;
        int totalAmenities = neighborhood.getAmenities().size();
        
        // Score based on number of amenities (more is generally better)
        score += Math.min(1.0, totalAmenities / 10.0);
        
        // Bonus for specific amenities based on user preferences
        if (user.getHobbies() != null) {
            if (user.getHobbies().contains(User.Hobby.FITNESS) && 
                neighborhood.getAmenities().contains(Neighborhood.Amenity.GYMS)) {
                score += 0.2;
            }
            if (user.getHobbies().contains(User.Hobby.GARDENING) && 
                neighborhood.getAmenities().contains(Neighborhood.Amenity.PARKS)) {
                score += 0.2;
            }
        }
        
        return Math.min(1.0, score);
    }
    
    /**
     * Calculate income compatibility based on user income level and neighborhood median income
     */
    private double calculateIncomeCompatibility(User.IncomeLevel userIncome, Double neighborhoodIncome) {
        double incomeThreshold;
        switch (userIncome) {
            case LOW:
                incomeThreshold = 50000;
                break;
            case MEDIUM:
                incomeThreshold = 75000;
                break;
            case HIGH:
                incomeThreshold = 100000;
                break;
            case VERY_HIGH:
                incomeThreshold = 150000;
                break;
            default:
                return 0.5;
        }
        
        double ratio = neighborhoodIncome / incomeThreshold;
        if (ratio >= 0.8 && ratio <= 1.2) {
            return 1.0; // Good match
        } else if (ratio >= 0.6 && ratio <= 1.4) {
            return 0.7; // Acceptable match
        } else {
            return 0.3; // Poor match
        }
    }
    
    /**
     * Calculate education compatibility
     */
    private double calculateEducationCompatibility(User.EducationLevel userEducation, Double neighborhoodCollegeRate) {
        double expectedRate;
        switch (userEducation) {
            case HIGH_SCHOOL:
                expectedRate = 0.3;
                break;
            case BACHELORS:
                expectedRate = 0.5;
                break;
            case MASTERS:
            case PHD:
                expectedRate = 0.7;
                break;
            default:
                return 0.5;
        }
        
        double difference = Math.abs(neighborhoodCollegeRate - expectedRate);
        if (difference <= 0.1) {
            return 1.0;
        } else if (difference <= 0.2) {
            return 0.7;
        } else {
            return 0.4;
        }
    }
    
    /**
     * Calculate location type compatibility
     */
    private double calculateLocationTypeCompatibility(User.LocationType userPreference, List<Neighborhood.LifestyleCharacteristic> neighborhoodCharacteristics) {
        if (neighborhoodCharacteristics == null || neighborhoodCharacteristics.isEmpty()) {
            return 0.5;
        }
        
        switch (userPreference) {
            case CITY_CENTER:
                return neighborhoodCharacteristics.contains(Neighborhood.LifestyleCharacteristic.URBAN) ? 1.0 : 0.3;
            case SUBURB:
                return neighborhoodCharacteristics.contains(Neighborhood.LifestyleCharacteristic.SUBURBAN) ? 1.0 : 0.3;
            case RURAL:
                return neighborhoodCharacteristics.contains(Neighborhood.LifestyleCharacteristic.RURAL) ? 1.0 : 0.3;
            case UNIVERSITY_AREA:
                return neighborhoodCharacteristics.contains(Neighborhood.LifestyleCharacteristic.UNIVERSITY_TOWN) ? 1.0 : 0.3;
            default:
                return 0.5;
        }
    }
    
    /**
     * Generate match reasoning based on scores
     */
    private String generateMatchReasoning(User user, Neighborhood neighborhood, double lifestyleScore, double demographicScore, double locationScore) {
        StringBuilder reasoning = new StringBuilder();
        
        reasoning.append("Based on your preferences, ").append(neighborhood.getName()).append(" offers ");
        
        if (lifestyleScore > 0.8) {
            reasoning.append("excellent lifestyle compatibility with your interests and family situation. ");
        } else if (lifestyleScore > 0.6) {
            reasoning.append("good lifestyle compatibility. ");
        } else {
            reasoning.append("some lifestyle differences to consider. ");
        }
        
        if (demographicScore > 0.8) {
            reasoning.append("The neighborhood demographics closely match your profile. ");
        } else if (demographicScore > 0.6) {
            reasoning.append("The neighborhood demographics are reasonably compatible. ");
        } else {
            reasoning.append("There are some demographic differences to consider. ");
        }
        
        if (locationScore > 0.8) {
            reasoning.append("The location and transportation options align well with your preferences. ");
        } else if (locationScore > 0.6) {
            reasoning.append("The location offers acceptable transportation options. ");
        } else {
            reasoning.append("The location may not fully meet your transportation needs. ");
        }
        
        return reasoning.toString();
    }
    
    /**
     * Generate recommendations based on match analysis
     */
    private String generateRecommendations(User user, Neighborhood neighborhood, double overallScore) {
        StringBuilder recommendations = new StringBuilder();
        
        if (overallScore > 80) {
            recommendations.append("This is an excellent match! Consider scheduling a visit to explore the area. ");
        } else if (overallScore > 70) {
            recommendations.append("This is a good match worth exploring further. ");
        } else if (overallScore > 60) {
            recommendations.append("This match has potential but consider your priorities carefully. ");
        } else {
            recommendations.append("This match may not be ideal for your needs. ");
        }
        
        // Add specific recommendations based on neighborhood characteristics
        if (neighborhood.getWalkScore() != null && neighborhood.getWalkScore() > 80) {
            recommendations.append("The area is highly walkable with many amenities within walking distance. ");
        }
        
        if (neighborhood.getSafetyScore() != null && neighborhood.getSafetyScore() > 8.0) {
            recommendations.append("The neighborhood has excellent safety ratings. ");
        }
        
        if (neighborhood.getNumberOfParks() != null && neighborhood.getNumberOfParks() > 5) {
            recommendations.append("There are many parks and green spaces in the area. ");
        }
        
        return recommendations.toString();
    }
} 