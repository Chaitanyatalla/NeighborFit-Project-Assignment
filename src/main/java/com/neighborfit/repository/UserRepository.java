package com.neighborfit.repository;

import com.neighborfit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity
 * 
 * Provides data access methods for user management and
 * custom queries for the matching algorithm.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Find users by age range
     */
    List<User> findByAgeBetween(Integer minAge, Integer maxAge);
    
    /**
     * Find users by income level
     */
    List<User> findByIncomeLevel(User.IncomeLevel incomeLevel);
    
    /**
     * Find users by lifestyle preferences
     */
    @Query("SELECT u FROM User u JOIN u.lifestylePreferences lp WHERE lp IN :preferences")
    List<User> findByLifestylePreferences(@Param("preferences") List<User.LifestylePreference> preferences);
    
    /**
     * Find users by family status
     */
    List<User> findByFamilyStatus(User.FamilyStatus familyStatus);
    
    /**
     * Find users by budget range
     */
    @Query("SELECT u FROM User u WHERE u.maxBudget BETWEEN :minBudget AND :maxBudget")
    List<User> findByBudgetRange(@Param("minBudget") Integer minBudget, @Param("maxBudget") Integer maxBudget);
    
    /**
     * Find users by location preference
     */
    List<User> findByPreferredLocationType(User.LocationType locationType);
    
    /**
     * Find users by multiple criteria for matching
     */
    @Query("SELECT u FROM User u WHERE " +
           "u.age BETWEEN :minAge AND :maxAge AND " +
           "u.incomeLevel = :incomeLevel AND " +
           "u.familyStatus = :familyStatus")
    List<User> findUsersForMatching(
        @Param("minAge") Integer minAge,
        @Param("maxAge") Integer maxAge,
        @Param("incomeLevel") User.IncomeLevel incomeLevel,
        @Param("familyStatus") User.FamilyStatus familyStatus
    );
} 