package com.neighborfit.service;

import com.neighborfit.model.Neighborhood;
import com.neighborfit.repository.NeighborhoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for neighborhood management and business logic
 * 
 * Handles neighborhood data management, filtering, and provides
 * data access methods for the matching algorithm.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NeighborhoodService {
    
    private final NeighborhoodRepository neighborhoodRepository;
    
    /**
     * Create a new neighborhood
     */
    public Neighborhood createNeighborhood(Neighborhood neighborhood) {
        log.info("Creating new neighborhood: {}", neighborhood.getName());
        
        Neighborhood savedNeighborhood = neighborhoodRepository.save(neighborhood);
        log.info("Neighborhood created successfully with ID: {}", savedNeighborhood.getId());
        return savedNeighborhood;
    }
    
    /**
     * Get neighborhood by ID
     */
    @Transactional(readOnly = true)
    public Optional<Neighborhood> getNeighborhoodById(Long id) {
        return neighborhoodRepository.findById(id);
    }
    
    /**
     * Get all neighborhoods
     */
    @Transactional(readOnly = true)
    public List<Neighborhood> getAllNeighborhoods() {
        return neighborhoodRepository.findAll();
    }
    
    /**
     * Get neighborhoods by city and state
     */
    @Transactional(readOnly = true)
    public List<Neighborhood> getNeighborhoodsByCityAndState(String city, String state) {
        return neighborhoodRepository.findByCityAndState(city, state);
    }
    
    /**
     * Get neighborhoods by ZIP code
     */
    @Transactional(readOnly = true)
    public List<Neighborhood> getNeighborhoodsByZipCode(String zipCode) {
        return neighborhoodRepository.findByZipCode(zipCode);
    }
    
    /**
     * Get neighborhoods by income range
     */
    @Transactional(readOnly = true)
    public List<Neighborhood> getNeighborhoodsByIncomeRange(Double minIncome, Double maxIncome) {
        return neighborhoodRepository.findByIncomeRange(minIncome, maxIncome);
    }
    
    /**
     * Get neighborhoods by home value range
     */
    @Transactional(readOnly = true)
    public List<Neighborhood> getNeighborhoodsByHomeValueRange(Double minValue, Double maxValue) {
        return neighborhoodRepository.findByHomeValueRange(minValue, maxValue);
    }
    
    /**
     * Get neighborhoods by rent range
     */
    @Transactional(readOnly = true)
    public List<Neighborhood> getNeighborhoodsByRentRange(Double minRent, Double maxRent) {
        return neighborhoodRepository.findByRentRange(minRent, maxRent);
    }
    
    /**
     * Get neighborhoods by maximum crime rate
     */
    @Transactional(readOnly = true)
    public List<Neighborhood> getNeighborhoodsByMaxCrimeRate(Double maxCrimeRate) {
        return neighborhoodRepository.findByMaxCrimeRate(maxCrimeRate);
    }
    
    /**
     * Get neighborhoods by minimum safety score
     */
    @Transactional(readOnly = true)
    public List<Neighborhood> getNeighborhoodsByMinSafetyScore(Double minSafetyScore) {
        return neighborhoodRepository.findByMinSafetyScore(minSafetyScore);
    }
    
    /**
     * Get neighborhoods by minimum walk score
     */
    @Transactional(readOnly = true)
    public List<Neighborhood> getNeighborhoodsByMinWalkScore(Double minWalkScore) {
        return neighborhoodRepository.findByMinWalkScore(minWalkScore);
    }
    
    /**
     * Get neighborhoods by minimum transit score
     */
    @Transactional(readOnly = true)
    public List<Neighborhood> getNeighborhoodsByMinTransitScore(Double minTransitScore) {
        return neighborhoodRepository.findByMinTransitScore(minTransitScore);
    }
    
    /**
     * Get neighborhoods by lifestyle characteristics
     */
    @Transactional(readOnly = true)
    public List<Neighborhood> getNeighborhoodsByLifestyleCharacteristics(List<Neighborhood.LifestyleCharacteristic> characteristics) {
        return neighborhoodRepository.findByLifestyleCharacteristics(characteristics);
    }
    
    /**
     * Get neighborhoods by amenities
     */
    @Transactional(readOnly = true)
    public List<Neighborhood> getNeighborhoodsByAmenities(List<Neighborhood.Amenity> amenities) {
        return neighborhoodRepository.findByAmenities(amenities);
    }
    
    /**
     * Get neighborhoods within geographic bounds
     */
    @Transactional(readOnly = true)
    public List<Neighborhood> getNeighborhoodsByGeographicBounds(Double minLat, Double maxLat, Double minLng, Double maxLng) {
        return neighborhoodRepository.findByGeographicBounds(minLat, maxLat, minLng, maxLng);
    }
    
    /**
     * Get neighborhoods for matching based on multiple criteria
     */
    @Transactional(readOnly = true)
    public List<Neighborhood> getNeighborhoodsForMatching(Double minIncome, Double maxIncome,
                                                        Double minHomeValue, Double maxHomeValue,
                                                        Double maxCrimeRate, Double minSafetyScore) {
        return neighborhoodRepository.findNeighborhoodsForMatching(minIncome, maxIncome, minHomeValue, maxHomeValue, maxCrimeRate, minSafetyScore);
    }
    
    /**
     * Update neighborhood
     */
    public Neighborhood updateNeighborhood(Long neighborhoodId, Neighborhood updateData) {
        log.info("Updating neighborhood with ID: {}", neighborhoodId);
        
        Neighborhood existingNeighborhood = neighborhoodRepository.findById(neighborhoodId)
                .orElseThrow(() -> new IllegalArgumentException("Neighborhood not found with ID: " + neighborhoodId));
        
        // Update fields
        existingNeighborhood.setName(updateData.getName());
        existingNeighborhood.setCity(updateData.getCity());
        existingNeighborhood.setState(updateData.getState());
        existingNeighborhood.setZipCode(updateData.getZipCode());
        existingNeighborhood.setLatitude(updateData.getLatitude());
        existingNeighborhood.setLongitude(updateData.getLongitude());
        existingNeighborhood.setTotalPopulation(updateData.getTotalPopulation());
        existingNeighborhood.setMedianAge(updateData.getMedianAge());
        existingNeighborhood.setMedianIncome(updateData.getMedianIncome());
        existingNeighborhood.setHomeOwnershipRate(updateData.getHomeOwnershipRate());
        existingNeighborhood.setCollegeGraduateRate(updateData.getCollegeGraduateRate());
        existingNeighborhood.setMedianHomeValue(updateData.getMedianHomeValue());
        existingNeighborhood.setMedianRent(updateData.getMedianRent());
        existingNeighborhood.setVacancyRate(updateData.getVacancyRate());
        existingNeighborhood.setLifestyleCharacteristics(updateData.getLifestyleCharacteristics());
        existingNeighborhood.setAmenities(updateData.getAmenities());
        existingNeighborhood.setTransportationOptions(updateData.getTransportationOptions());
        existingNeighborhood.setCrimeRate(updateData.getCrimeRate());
        existingNeighborhood.setSafetyScore(updateData.getSafetyScore());
        existingNeighborhood.setSchoolRating(updateData.getSchoolRating());
        existingNeighborhood.setNumberOfSchools(updateData.getNumberOfSchools());
        existingNeighborhood.setUnemploymentRate(updateData.getUnemploymentRate());
        existingNeighborhood.setCommuteTimeMinutes(updateData.getCommuteTimeMinutes());
        existingNeighborhood.setAirQualityIndex(updateData.getAirQualityIndex());
        existingNeighborhood.setWalkScore(updateData.getWalkScore());
        existingNeighborhood.setBikeScore(updateData.getBikeScore());
        existingNeighborhood.setTransitScore(updateData.getTransitScore());
        existingNeighborhood.setDiversityIndex(updateData.getDiversityIndex());
        existingNeighborhood.setNumberOfRestaurants(updateData.getNumberOfRestaurants());
        existingNeighborhood.setNumberOfParks(updateData.getNumberOfParks());
        existingNeighborhood.setNumberOfLibraries(updateData.getNumberOfLibraries());
        
        Neighborhood updatedNeighborhood = neighborhoodRepository.save(existingNeighborhood);
        log.info("Neighborhood updated successfully with ID: {}", neighborhoodId);
        return updatedNeighborhood;
    }
    
    /**
     * Delete neighborhood
     */
    public void deleteNeighborhood(Long neighborhoodId) {
        log.info("Deleting neighborhood with ID: {}", neighborhoodId);
        
        if (!neighborhoodRepository.existsById(neighborhoodId)) {
            throw new IllegalArgumentException("Neighborhood not found with ID: " + neighborhoodId);
        }
        
        neighborhoodRepository.deleteById(neighborhoodId);
        log.info("Neighborhood deleted successfully with ID: {}", neighborhoodId);
    }
    
    /**
     * Validate neighborhood data for matching
     */
    public boolean validateNeighborhoodForMatching(Neighborhood neighborhood) {
        if (neighborhood == null) {
            return false;
        }
        
        // Check required fields for matching
        return neighborhood.getMedianIncome() != null &&
               neighborhood.getMedianHomeValue() != null &&
               neighborhood.getCrimeRate() != null &&
               neighborhood.getSafetyScore() != null;
    }
    
    /**
     * Get top neighborhoods by safety score
     */
    @Transactional(readOnly = true)
    public List<Neighborhood> getTopNeighborhoodsBySafety(int limit) {
        return neighborhoodRepository.findAll().stream()
                .filter(n -> n.getSafetyScore() != null)
                .sorted((n1, n2) -> Double.compare(n2.getSafetyScore(), n1.getSafetyScore()))
                .limit(limit)
                .toList();
    }
    
    /**
     * Get top neighborhoods by walk score
     */
    @Transactional(readOnly = true)
    public List<Neighborhood> getTopNeighborhoodsByWalkability(int limit) {
        return neighborhoodRepository.findAll().stream()
                .filter(n -> n.getWalkScore() != null)
                .sorted((n1, n2) -> Double.compare(n2.getWalkScore(), n1.getWalkScore()))
                .limit(limit)
                .toList();
    }
} 