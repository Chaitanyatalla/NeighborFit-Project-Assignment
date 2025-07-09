package com.neighborfit.service;

import com.neighborfit.dto.MatchResultDto;
import com.neighborfit.model.Match;
import com.neighborfit.model.Neighborhood;
import com.neighborfit.model.User;
import com.neighborfit.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Main matching service that orchestrates the neighborhood-lifestyle matching process
 * 
 * This service coordinates between users, neighborhoods, and the matching algorithm
 * to provide comprehensive matching results and analytics.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MatchingService {
    
    private final UserService userService;
    private final NeighborhoodService neighborhoodService;
    private final MatchingAlgorithm matchingAlgorithm;
    private final MatchRepository matchRepository;
    
    /**
     * Find matches for a specific user
     */
    public List<MatchResultDto> findMatchesForUser(Long userId, int limit) {
        log.info("Finding matches for user ID: {} with limit: {}", userId, limit);
        
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        if (!userService.validateUserForMatching(user)) {
            throw new IllegalArgumentException("User profile incomplete for matching");
        }
        
        // Get candidate neighborhoods based on user preferences
        List<Neighborhood> candidateNeighborhoods = getCandidateNeighborhoods(user);
        
        // Calculate matches for all candidates
        List<Match> matches = candidateNeighborhoods.stream()
                .map(neighborhood -> matchingAlgorithm.calculateMatch(user, neighborhood))
                .sorted((m1, m2) -> Double.compare(m2.getOverallScore(), m1.getOverallScore()))
                .limit(limit)
                .collect(Collectors.toList());
        
        // Save matches to database
        List<Match> savedMatches = matchRepository.saveAll(matches);
        
        // Convert to DTOs
        List<MatchResultDto> matchResults = savedMatches.stream()
                .map(this::convertToMatchResultDto)
                .collect(Collectors.toList());
        
        log.info("Found {} matches for user ID: {}", matchResults.size(), userId);
        return matchResults;
    }
    
    /**
     * Find matches for all users
     */
    public List<MatchResultDto> findMatchesForAllUsers(int limitPerUser) {
        log.info("Finding matches for all users with limit per user: {}", limitPerUser);
        
        List<User> users = userService.getAllUsers();
        List<MatchResultDto> allMatches = users.stream()
                .filter(userService::validateUserForMatching)
                .flatMap(user -> findMatchesForUser(user.getId(), limitPerUser).stream())
                .collect(Collectors.toList());
        
        log.info("Found {} total matches for all users", allMatches.size());
        return allMatches;
    }
    
    /**
     * Get match history for a user
     */
    @Transactional(readOnly = true)
    public List<MatchResultDto> getMatchHistoryForUser(Long userId) {
        log.info("Getting match history for user ID: {}", userId);
        
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        List<Match> matches = matchRepository.findByUserOrderByOverallScoreDesc(user);
        
        return matches.stream()
                .map(this::convertToMatchResultDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get top matches for a user
     */
    @Transactional(readOnly = true)
    public List<MatchResultDto> getTopMatchesForUser(Long userId, int limit) {
        log.info("Getting top {} matches for user ID: {}", limit, userId);
        
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        List<Match> matches = matchRepository.findTopMatchesForUser(user, limit);
        
        return matches.stream()
                .map(this::convertToMatchResultDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get matches by strength
     */
    @Transactional(readOnly = true)
    public List<MatchResultDto> getMatchesByStrength(Match.MatchStrength strength) {
        log.info("Getting matches by strength: {}", strength);
        
        List<Match> matches = matchRepository.findByMatchStrengthOrderByOverallScoreDesc(strength);
        
        return matches.stream()
                .map(this::convertToMatchResultDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get recent matches
     */
    @Transactional(readOnly = true)
    public List<MatchResultDto> getRecentMatches(int limit) {
        log.info("Getting recent {} matches", limit);
        
        List<Match> matches = matchRepository.findRecentMatches(limit);
        
        return matches.stream()
                .map(this::convertToMatchResultDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Update user feedback for a match
     */
    public void updateMatchFeedback(Long matchId, Boolean userLiked, Boolean userVisited, 
                                   Integer userRating, String userFeedback) {
        log.info("Updating feedback for match ID: {}", matchId);
        
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with ID: " + matchId));
        
        match.setUserLiked(userLiked);
        match.setUserVisited(userVisited);
        match.setUserRating(userRating);
        match.setUserFeedback(userFeedback);
        
        matchRepository.save(match);
        log.info("Match feedback updated successfully for match ID: {}", matchId);
    }
    
    /**
     * Get match analytics
     */
    @Transactional(readOnly = true)
    public MatchAnalytics getMatchAnalytics() {
        log.info("Getting match analytics");
        
        long totalMatches = matchRepository.count();
        long matchesWithFeedback = matchRepository.findByUserLikedIsNotNull().size();
        long matchesWithRatings = matchRepository.findMatchesWithRatings().size();
        
        // Get average scores
        Double avgOverallScore = matchRepository.findAll().stream()
                .mapToDouble(Match::getOverallScore)
                .average()
                .orElse(0.0);
        
        Double avgLifestyleScore = matchRepository.findAll().stream()
                .mapToDouble(Match::getLifestyleScore)
                .average()
                .orElse(0.0);
        
        Double avgDemographicScore = matchRepository.findAll().stream()
                .mapToDouble(Match::getDemographicScore)
                .average()
                .orElse(0.0);
        
        Double avgLocationScore = matchRepository.findAll().stream()
                .mapToDouble(Match::getLocationScore)
                .average()
                .orElse(0.0);
        
        return MatchAnalytics.builder()
                .totalMatches(totalMatches)
                .matchesWithFeedback(matchesWithFeedback)
                .matchesWithRatings(matchesWithRatings)
                .averageOverallScore(avgOverallScore)
                .averageLifestyleScore(avgLifestyleScore)
                .averageDemographicScore(avgDemographicScore)
                .averageLocationScore(avgLocationScore)
                .build();
    }
    
    /**
     * Get candidate neighborhoods based on user preferences
     */
    private List<Neighborhood> getCandidateNeighborhoods(User user) {
        // Convert user income level to numeric range
        double minIncome = getMinIncomeForLevel(user.getIncomeLevel());
        double maxIncome = getMaxIncomeForLevel(user.getIncomeLevel());
        
        // Convert user budget to home value range (assuming 20% down payment)
        double minHomeValue = user.getMinBudget() != null ? user.getMinBudget() * 0.8 : 0.0;
        double maxHomeValue = user.getMaxBudget() != null ? user.getMaxBudget() * 1.2 : Double.MAX_VALUE;
        
        // Get neighborhoods that meet basic criteria
        List<Neighborhood> candidates = neighborhoodService.getNeighborhoodsForMatching(
                minIncome, maxIncome, minHomeValue, maxHomeValue, 0.1, 6.0);
        
        // If not enough candidates, relax constraints
        if (candidates.size() < 10) {
            candidates = neighborhoodService.getAllNeighborhoods();
        }
        
        return candidates;
    }
    
    /**
     * Convert income level to numeric range
     */
    private double getMinIncomeForLevel(User.IncomeLevel incomeLevel) {
        if (incomeLevel == null) return 0.0;
        
        switch (incomeLevel) {
            case LOW: return 0.0;
            case MEDIUM: return 50000.0;
            case HIGH: return 75000.0;
            case VERY_HIGH: return 100000.0;
            default: return 0.0;
        }
    }
    
    /**
     * Convert income level to numeric range
     */
    private double getMaxIncomeForLevel(User.IncomeLevel incomeLevel) {
        if (incomeLevel == null) return Double.MAX_VALUE;
        
        switch (incomeLevel) {
            case LOW: return 50000.0;
            case MEDIUM: return 75000.0;
            case HIGH: return 100000.0;
            case VERY_HIGH: return Double.MAX_VALUE;
            default: return Double.MAX_VALUE;
        }
    }
    
    /**
     * Convert Match entity to MatchResultDto
     */
    private MatchResultDto convertToMatchResultDto(Match match) {
        return MatchResultDto.builder()
                .matchId(match.getId())
                .neighborhoodId(match.getNeighborhood().getId())
                .neighborhoodName(match.getNeighborhood().getName())
                .city(match.getNeighborhood().getCity())
                .state(match.getNeighborhood().getState())
                .zipCode(match.getNeighborhood().getZipCode())
                .overallScore(match.getOverallScore())
                .lifestyleScore(match.getLifestyleScore())
                .demographicScore(match.getDemographicScore())
                .locationScore(match.getLocationScore())
                .budgetScore(match.getBudgetScore())
                .amenityScore(match.getAmenityScore())
                .matchStrength(match.getMatchStrength())
                .matchReasoning(match.getMatchReasoning())
                .recommendations(match.getRecommendations())
                .medianIncome(match.getNeighborhood().getMedianIncome())
                .medianHomeValue(match.getNeighborhood().getMedianHomeValue())
                .medianRent(match.getNeighborhood().getMedianRent())
                .crimeRate(match.getNeighborhood().getCrimeRate())
                .safetyScore(match.getNeighborhood().getSafetyScore())
                .walkScore(match.getNeighborhood().getWalkScore())
                .transitScore(match.getNeighborhood().getTransitScore())
                .numberOfRestaurants(match.getNeighborhood().getNumberOfRestaurants())
                .numberOfParks(match.getNeighborhood().getNumberOfParks())
                .createdAt(match.getCreatedAt())
                .build();
    }
    
    /**
     * Analytics data class for match statistics
     */
    public static class MatchAnalytics {
        private final long totalMatches;
        private final long matchesWithFeedback;
        private final long matchesWithRatings;
        private final double averageOverallScore;
        private final double averageLifestyleScore;
        private final double averageDemographicScore;
        private final double averageLocationScore;
        
        private MatchAnalytics(Builder builder) {
            this.totalMatches = builder.totalMatches;
            this.matchesWithFeedback = builder.matchesWithFeedback;
            this.matchesWithRatings = builder.matchesWithRatings;
            this.averageOverallScore = builder.averageOverallScore;
            this.averageLifestyleScore = builder.averageLifestyleScore;
            this.averageDemographicScore = builder.averageDemographicScore;
            this.averageLocationScore = builder.averageLocationScore;
        }
        
        // Getters
        public long getTotalMatches() { return totalMatches; }
        public long getMatchesWithFeedback() { return matchesWithFeedback; }
        public long getMatchesWithRatings() { return matchesWithRatings; }
        public double getAverageOverallScore() { return averageOverallScore; }
        public double getAverageLifestyleScore() { return averageLifestyleScore; }
        public double getAverageDemographicScore() { return averageDemographicScore; }
        public double getAverageLocationScore() { return averageLocationScore; }
        
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private long totalMatches;
            private long matchesWithFeedback;
            private long matchesWithRatings;
            private double averageOverallScore;
            private double averageLifestyleScore;
            private double averageDemographicScore;
            private double averageLocationScore;
            
            public Builder totalMatches(long totalMatches) {
                this.totalMatches = totalMatches;
                return this;
            }
            
            public Builder matchesWithFeedback(long matchesWithFeedback) {
                this.matchesWithFeedback = matchesWithFeedback;
                return this;
            }
            
            public Builder matchesWithRatings(long matchesWithRatings) {
                this.matchesWithRatings = matchesWithRatings;
                return this;
            }
            
            public Builder averageOverallScore(double averageOverallScore) {
                this.averageOverallScore = averageOverallScore;
                return this;
            }
            
            public Builder averageLifestyleScore(double averageLifestyleScore) {
                this.averageLifestyleScore = averageLifestyleScore;
                return this;
            }
            
            public Builder averageDemographicScore(double averageDemographicScore) {
                this.averageDemographicScore = averageDemographicScore;
                return this;
            }
            
            public Builder averageLocationScore(double averageLocationScore) {
                this.averageLocationScore = averageLocationScore;
                return this;
            }
            
            public MatchAnalytics build() {
                return new MatchAnalytics(this);
            }
        }
    }
} 