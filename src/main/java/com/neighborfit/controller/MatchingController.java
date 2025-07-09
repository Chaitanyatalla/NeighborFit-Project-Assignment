package com.neighborfit.controller;

import com.neighborfit.dto.MatchResultDto;
import com.neighborfit.model.Match;
import com.neighborfit.service.MatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API controller for matching operations
 * 
 * Provides endpoints for finding matches, retrieving match history,
 * and managing user feedback on matches.
 */
@RestController
@RequestMapping("/matching")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class MatchingController {
    
    private final MatchingService matchingService;
    
    /**
     * Find matches for a specific user
     */
    @PostMapping("/users/{userId}/matches")
    public ResponseEntity<List<MatchResultDto>> findMatchesForUser(@PathVariable Long userId,
                                                                  @RequestParam(defaultValue = "10") int limit) {
        try {
            log.info("Finding matches for user ID: {} with limit: {}", userId, limit);
            List<MatchResultDto> matches = matchingService.findMatchesForUser(userId, limit);
            return ResponseEntity.ok(matches);
        } catch (IllegalArgumentException e) {
            log.error("Match finding failed: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error during match finding: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Find matches for all users
     */
    @PostMapping("/all-users/matches")
    public ResponseEntity<List<MatchResultDto>> findMatchesForAllUsers(@RequestParam(defaultValue = "5") int limitPerUser) {
        try {
            log.info("Finding matches for all users with limit per user: {}", limitPerUser);
            List<MatchResultDto> matches = matchingService.findMatchesForAllUsers(limitPerUser);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            log.error("Unexpected error during bulk match finding: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get match history for a user
     */
    @GetMapping("/users/{userId}/history")
    public ResponseEntity<List<MatchResultDto>> getMatchHistoryForUser(@PathVariable Long userId) {
        try {
            log.info("Getting match history for user ID: {}", userId);
            List<MatchResultDto> matches = matchingService.getMatchHistoryForUser(userId);
            return ResponseEntity.ok(matches);
        } catch (IllegalArgumentException e) {
            log.error("Failed to get match history: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error getting match history: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get top matches for a user
     */
    @GetMapping("/users/{userId}/top-matches")
    public ResponseEntity<List<MatchResultDto>> getTopMatchesForUser(@PathVariable Long userId,
                                                                    @RequestParam(defaultValue = "5") int limit) {
        try {
            log.info("Getting top {} matches for user ID: {}", limit, userId);
            List<MatchResultDto> matches = matchingService.getTopMatchesForUser(userId, limit);
            return ResponseEntity.ok(matches);
        } catch (IllegalArgumentException e) {
            log.error("Failed to get top matches: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error getting top matches: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get matches by strength
     */
    @GetMapping("/strength/{strength}")
    public ResponseEntity<List<MatchResultDto>> getMatchesByStrength(@PathVariable Match.MatchStrength strength) {
        try {
            log.info("Getting matches by strength: {}", strength);
            List<MatchResultDto> matches = matchingService.getMatchesByStrength(strength);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            log.error("Unexpected error getting matches by strength: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get recent matches
     */
    @GetMapping("/recent")
    public ResponseEntity<List<MatchResultDto>> getRecentMatches(@RequestParam(defaultValue = "10") int limit) {
        try {
            log.info("Getting recent {} matches", limit);
            List<MatchResultDto> matches = matchingService.getRecentMatches(limit);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            log.error("Unexpected error getting recent matches: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Update user feedback for a match
     */
    @PutMapping("/matches/{matchId}/feedback")
    public ResponseEntity<Void> updateMatchFeedback(@PathVariable Long matchId,
                                                   @RequestParam(required = false) Boolean userLiked,
                                                   @RequestParam(required = false) Boolean userVisited,
                                                   @RequestParam(required = false) Integer userRating,
                                                   @RequestParam(required = false) String userFeedback) {
        try {
            log.info("Updating feedback for match ID: {}", matchId);
            matchingService.updateMatchFeedback(matchId, userLiked, userVisited, userRating, userFeedback);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.error("Failed to update match feedback: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error updating match feedback: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get match analytics
     */
    @GetMapping("/analytics")
    public ResponseEntity<MatchingService.MatchAnalytics> getMatchAnalytics() {
        try {
            log.info("Getting match analytics");
            MatchingService.MatchAnalytics analytics = matchingService.getMatchAnalytics();
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            log.error("Unexpected error getting match analytics: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Matching service is healthy");
    }
} 