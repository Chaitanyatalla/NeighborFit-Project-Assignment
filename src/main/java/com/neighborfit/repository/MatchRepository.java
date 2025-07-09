package com.neighborfit.repository;

import com.neighborfit.model.Match;
import com.neighborfit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Match entity
 * 
 * Provides data access methods for match management and
 * analytics queries for the matching algorithm.
 */
@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    
    /**
     * Find matches by user
     */
    List<Match> findByUserOrderByOverallScoreDesc(User user);
    
    /**
     * Find matches by user with minimum score
     */
    @Query("SELECT m FROM Match m WHERE m.user = :user AND m.overallScore >= :minScore ORDER BY m.overallScore DESC")
    List<Match> findByUserAndMinScore(@Param("user") User user, @Param("minScore") Double minScore);
    
    /**
     * Find matches by match strength
     */
    List<Match> findByMatchStrengthOrderByOverallScoreDesc(Match.MatchStrength matchStrength);
    
    /**
     * Find top matches for a user
     */
    @Query("SELECT m FROM Match m WHERE m.user = :user ORDER BY m.overallScore DESC LIMIT :limit")
    List<Match> findTopMatchesForUser(@Param("user") User user, @Param("limit") int limit);
    
    /**
     * Find matches by neighborhood
     */
    @Query("SELECT m FROM Match m WHERE m.neighborhood.id = :neighborhoodId ORDER BY m.overallScore DESC")
    List<Match> findByNeighborhoodOrderByOverallScoreDesc(@Param("neighborhoodId") Long neighborhoodId);
    
    /**
     * Find matches with user feedback
     */
    List<Match> findByUserLikedIsNotNull();
    
    /**
     * Find matches with user ratings
     */
    @Query("SELECT m FROM Match m WHERE m.userRating IS NOT NULL ORDER BY m.userRating DESC")
    List<Match> findMatchesWithRatings();
    
    /**
     * Find average match score for a user
     */
    @Query("SELECT AVG(m.overallScore) FROM Match m WHERE m.user = :user")
    Double findAverageMatchScoreForUser(@Param("user") User user);
    
    /**
     * Find average match score for a neighborhood
     */
    @Query("SELECT AVG(m.overallScore) FROM Match m WHERE m.neighborhood.id = :neighborhoodId")
    Double findAverageMatchScoreForNeighborhood(@Param("neighborhoodId") Long neighborhoodId);
    
    /**
     * Find matches by score range
     */
    @Query("SELECT m FROM Match m WHERE m.overallScore BETWEEN :minScore AND :maxScore ORDER BY m.overallScore DESC")
    List<Match> findByScoreRange(@Param("minScore") Double minScore, @Param("maxScore") Double maxScore);
    
    /**
     * Find matches by component score
     */
    @Query("SELECT m FROM Match m WHERE m.lifestyleScore >= :minLifestyleScore ORDER BY m.lifestyleScore DESC")
    List<Match> findByMinLifestyleScore(@Param("minLifestyleScore") Double minLifestyleScore);
    
    /**
     * Find matches by demographic score
     */
    @Query("SELECT m FROM Match m WHERE m.demographicScore >= :minDemographicScore ORDER BY m.demographicScore DESC")
    List<Match> findByMinDemographicScore(@Param("minDemographicScore") Double minDemographicScore);
    
    /**
     * Find matches by location score
     */
    @Query("SELECT m FROM Match m WHERE m.locationScore >= :minLocationScore ORDER BY m.locationScore DESC")
    List<Match> findByMinLocationScore(@Param("minLocationScore") Double minLocationScore);
    
    /**
     * Find matches by budget score
     */
    @Query("SELECT m FROM Match m WHERE m.budgetScore >= :minBudgetScore ORDER BY m.budgetScore DESC")
    List<Match> findByMinBudgetScore(@Param("minBudgetScore") Double minBudgetScore);
    
    /**
     * Count matches by match strength
     */
    @Query("SELECT m.matchStrength, COUNT(m) FROM Match m GROUP BY m.matchStrength")
    List<Object[]> countMatchesByStrength();
    
    /**
     * Find recent matches
     */
    @Query("SELECT m FROM Match m ORDER BY m.createdAt DESC LIMIT :limit")
    List<Match> findRecentMatches(@Param("limit") int limit);
} 