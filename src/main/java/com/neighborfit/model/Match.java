package com.neighborfit.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Match entity representing the relationship between users and neighborhoods
 * 
 * This model stores the results of the matching algorithm, including
 * compatibility scores and detailed breakdowns of the match.
 */
@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighborhood_id", nullable = false)
    private Neighborhood neighborhood;
    
    // Overall Match Score (0-100)
    @Column(nullable = false)
    private Double overallScore;
    
    // Detailed Component Scores
    private Double lifestyleScore;
    private Double demographicScore;
    private Double locationScore;
    private Double budgetScore;
    private Double amenityScore;
    
    // Match Details
    @Enumerated(EnumType.STRING)
    private MatchStrength matchStrength;
    
    @Column(length = 1000)
    private String matchReasoning;
    
    @Column(length = 1000)
    private String recommendations;
    
    // User Feedback
    private Boolean userLiked;
    private Boolean userVisited;
    private Integer userRating;
    
    @Column(length = 500)
    private String userFeedback;
    
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
    
    // Enum for match strength
    public enum MatchStrength {
        EXCELLENT(90, 100, "Excellent match - Highly recommended"),
        VERY_GOOD(80, 89, "Very good match - Strongly recommended"),
        GOOD(70, 79, "Good match - Recommended"),
        FAIR(60, 69, "Fair match - Consider with caution"),
        POOR(0, 59, "Poor match - Not recommended");
        
        private final int minScore;
        private final int maxScore;
        private final String description;
        
        MatchStrength(int minScore, int maxScore, String description) {
            this.minScore = minScore;
            this.maxScore = maxScore;
            this.description = description;
        }
        
        public static MatchStrength fromScore(double score) {
            for (MatchStrength strength : values()) {
                if (score >= strength.minScore && score <= strength.maxScore) {
                    return strength;
                }
            }
            return POOR;
        }
        
        public String getDescription() {
            return description;
        }
    }
} 