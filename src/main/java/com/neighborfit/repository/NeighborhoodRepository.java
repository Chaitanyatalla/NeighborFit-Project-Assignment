package com.neighborfit.repository;

import com.neighborfit.model.Neighborhood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Neighborhood entity
 * 
 * Provides data access methods for neighborhood management and
 * custom queries for the matching algorithm.
 */
@Repository
public interface NeighborhoodRepository extends JpaRepository<Neighborhood, Long> {
    
    /**
     * Find neighborhoods by city and state
     */
    List<Neighborhood> findByCityAndState(String city, String state);
    
    /**
     * Find neighborhoods by ZIP code
     */
    List<Neighborhood> findByZipCode(String zipCode);
    
    /**
     * Find neighborhoods by income range
     */
    @Query("SELECT n FROM Neighborhood n WHERE n.medianIncome BETWEEN :minIncome AND :maxIncome")
    List<Neighborhood> findByIncomeRange(@Param("minIncome") Double minIncome, @Param("maxIncome") Double maxIncome);
    
    /**
     * Find neighborhoods by home value range
     */
    @Query("SELECT n FROM Neighborhood n WHERE n.medianHomeValue BETWEEN :minValue AND :maxValue")
    List<Neighborhood> findByHomeValueRange(@Param("minValue") Double minValue, @Param("maxValue") Double maxValue);
    
    /**
     * Find neighborhoods by rent range
     */
    @Query("SELECT n FROM Neighborhood n WHERE n.medianRent BETWEEN :minRent AND :maxRent")
    List<Neighborhood> findByRentRange(@Param("minRent") Double minRent, @Param("maxRent") Double maxRent);
    
    /**
     * Find neighborhoods by crime rate (lower is better)
     */
    @Query("SELECT n FROM Neighborhood n WHERE n.crimeRate <= :maxCrimeRate ORDER BY n.crimeRate ASC")
    List<Neighborhood> findByMaxCrimeRate(@Param("maxCrimeRate") Double maxCrimeRate);
    
    /**
     * Find neighborhoods by safety score (higher is better)
     */
    @Query("SELECT n FROM Neighborhood n WHERE n.safetyScore >= :minSafetyScore ORDER BY n.safetyScore DESC")
    List<Neighborhood> findByMinSafetyScore(@Param("minSafetyScore") Double minSafetyScore);
    
    /**
     * Find neighborhoods by walk score
     */
    @Query("SELECT n FROM Neighborhood n WHERE n.walkScore >= :minWalkScore ORDER BY n.walkScore DESC")
    List<Neighborhood> findByMinWalkScore(@Param("minWalkScore") Double minWalkScore);
    
    /**
     * Find neighborhoods by transit score
     */
    @Query("SELECT n FROM Neighborhood n WHERE n.transitScore >= :minTransitScore ORDER BY n.transitScore DESC")
    List<Neighborhood> findByMinTransitScore(@Param("minTransitScore") Double minTransitScore);
    
    /**
     * Find neighborhoods by lifestyle characteristics
     */
    @Query("SELECT n FROM Neighborhood n JOIN n.lifestyleCharacteristics lc WHERE lc IN :characteristics")
    List<Neighborhood> findByLifestyleCharacteristics(@Param("characteristics") List<Neighborhood.LifestyleCharacteristic> characteristics);
    
    /**
     * Find neighborhoods by amenities
     */
    @Query("SELECT n FROM Neighborhood n JOIN n.amenities a WHERE a IN :amenities")
    List<Neighborhood> findByAmenities(@Param("amenities") List<Neighborhood.Amenity> amenities);
    
    /**
     * Find neighborhoods within geographic bounds
     */
    @Query("SELECT n FROM Neighborhood n WHERE " +
           "n.latitude BETWEEN :minLat AND :maxLat AND " +
           "n.longitude BETWEEN :minLng AND :maxLng")
    List<Neighborhood> findByGeographicBounds(
        @Param("minLat") Double minLat,
        @Param("maxLat") Double maxLat,
        @Param("minLng") Double minLng,
        @Param("maxLng") Double maxLng
    );
    
    /**
     * Find neighborhoods by multiple criteria for matching
     */
    @Query("SELECT n FROM Neighborhood n WHERE " +
           "n.medianIncome BETWEEN :minIncome AND :maxIncome AND " +
           "n.medianHomeValue BETWEEN :minHomeValue AND :maxHomeValue AND " +
           "n.crimeRate <= :maxCrimeRate AND " +
           "n.safetyScore >= :minSafetyScore")
    List<Neighborhood> findNeighborhoodsForMatching(
        @Param("minIncome") Double minIncome,
        @Param("maxIncome") Double maxIncome,
        @Param("minHomeValue") Double minHomeValue,
        @Param("maxHomeValue") Double maxHomeValue,
        @Param("maxCrimeRate") Double maxCrimeRate,
        @Param("minSafetyScore") Double minSafetyScore
    );
} 