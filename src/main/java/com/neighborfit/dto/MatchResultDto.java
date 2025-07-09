package com.neighborfit.dto;

import com.neighborfit.model.Match;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for match results
 * 
 * This DTO provides a clean interface for returning match results
 * to the frontend, including all relevant information about the match.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchResultDto {
    
    private Long matchId;
    private Long neighborhoodId;
    private String neighborhoodName;
    private String city;
    private String state;
    private String zipCode;
    
    // Scores
    private Double overallScore;
    private Double lifestyleScore;
    private Double demographicScore;
    private Double locationScore;
    private Double budgetScore;
    private Double amenityScore;
    
    // Match Details
    private Match.MatchStrength matchStrength;
    private String matchReasoning;
    private String recommendations;
    
    // Neighborhood Details
    private Double medianIncome;
    private Double medianHomeValue;
    private Double medianRent;
    private Double crimeRate;
    private Double safetyScore;
    private Double walkScore;
    private Double transitScore;
    private Integer numberOfRestaurants;
    private Integer numberOfParks;
    
    // Timestamps
    private LocalDateTime createdAt;
} 