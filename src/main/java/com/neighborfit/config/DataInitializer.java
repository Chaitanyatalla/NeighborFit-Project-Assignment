package com.neighborfit.config;

import com.neighborfit.model.Neighborhood;
import com.neighborfit.model.User;
import com.neighborfit.repository.NeighborhoodRepository;
import com.neighborfit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Data initializer for populating the database with sample data
 * 
 * This component runs on application startup to create sample users
 * and neighborhoods for testing the matching algorithm.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final NeighborhoodRepository neighborhoodRepository;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing sample data...");
        
        // Initialize sample neighborhoods
        initializeNeighborhoods();
        
        // Initialize sample users
        initializeUsers();
        
        log.info("Sample data initialization completed!");
    }
    
    private void initializeNeighborhoods() {
        if (neighborhoodRepository.count() > 0) {
            log.info("Neighborhoods already exist, skipping initialization");
            return;
        }
        
        List<Neighborhood> neighborhoods = Arrays.asList(
            // Urban neighborhoods
            Neighborhood.builder()
                .name("Downtown Financial District")
                .city("New York")
                .state("NY")
                .zipCode("10005")
                .latitude(40.7061)
                .longitude(-74.0089)
                .totalPopulation(15000)
                .medianAge(32.5)
                .medianIncome(85000.0)
                .homeOwnershipRate(0.25)
                .collegeGraduateRate(0.75)
                .medianHomeValue(850000.0)
                .medianRent(3500.0)
                .vacancyRate(0.05)
                .lifestyleCharacteristics(Arrays.asList(
                    Neighborhood.LifestyleCharacteristic.URBAN,
                    Neighborhood.LifestyleCharacteristic.YOUNG_PROFESSIONAL
                ))
                .amenities(Arrays.asList(
                    Neighborhood.Amenity.RESTAURANTS,
                    Neighborhood.Amenity.SHOPPING_CENTERS,
                    Neighborhood.Amenity.GYMS,
                    Neighborhood.Amenity.COFFEE_SHOPS
                ))
                .transportationOptions(Arrays.asList(
                    Neighborhood.TransportationOption.SUBWAY,
                    Neighborhood.TransportationOption.BUS,
                    Neighborhood.TransportationOption.WALKING_TRAILS
                ))
                .crimeRate(0.08)
                .safetyScore(7.5)
                .schoolRating(8.2)
                .numberOfSchools(3)
                .unemploymentRate(0.04)
                .commuteTimeMinutes(25.0)
                .airQualityIndex(65.0)
                .walkScore(95.0)
                .bikeScore(85.0)
                .transitScore(90.0)
                .diversityIndex(0.85)
                .numberOfRestaurants(45)
                .numberOfParks(2)
                .numberOfLibraries(1)
                .build(),
            
            // Suburban family neighborhood
            Neighborhood.builder()
                .name("Maplewood Suburbs")
                .city("Austin")
                .state("TX")
                .zipCode("78759")
                .latitude(30.4016)
                .longitude(-97.7431)
                .totalPopulation(25000)
                .medianAge(38.2)
                .medianIncome(95000.0)
                .homeOwnershipRate(0.75)
                .collegeGraduateRate(0.65)
                .medianHomeValue(450000.0)
                .medianRent(2200.0)
                .vacancyRate(0.03)
                .lifestyleCharacteristics(Arrays.asList(
                    Neighborhood.LifestyleCharacteristic.SUBURBAN,
                    Neighborhood.LifestyleCharacteristic.FAMILY_FRIENDLY
                ))
                .amenities(Arrays.asList(
                    Neighborhood.Amenity.GROCERY_STORES,
                    Neighborhood.Amenity.PARKS,
                    Neighborhood.Amenity.LIBRARIES,
                    Neighborhood.Amenity.HOSPITALS
                ))
                .transportationOptions(Arrays.asList(
                    Neighborhood.TransportationOption.BUS,
                    Neighborhood.TransportationOption.BIKE_LANES,
                    Neighborhood.TransportationOption.PARKING
                ))
                .crimeRate(0.03)
                .safetyScore(9.2)
                .schoolRating(9.1)
                .numberOfSchools(8)
                .unemploymentRate(0.03)
                .commuteTimeMinutes(35.0)
                .airQualityIndex(75.0)
                .walkScore(45.0)
                .bikeScore(60.0)
                .transitScore(40.0)
                .diversityIndex(0.70)
                .numberOfRestaurants(25)
                .numberOfParks(12)
                .numberOfLibraries(2)
                .build(),
            
            // University area
            Neighborhood.builder()
                .name("University District")
                .city("Boston")
                .state("MA")
                .zipCode("02115")
                .latitude(42.3399)
                .longitude(-71.0899)
                .totalPopulation(18000)
                .medianAge(24.8)
                .medianIncome(65000.0)
                .homeOwnershipRate(0.15)
                .collegeGraduateRate(0.85)
                .medianHomeValue(650000.0)
                .medianRent(2800.0)
                .vacancyRate(0.08)
                .lifestyleCharacteristics(Arrays.asList(
                    Neighborhood.LifestyleCharacteristic.URBAN,
                    Neighborhood.LifestyleCharacteristic.UNIVERSITY_TOWN
                ))
                .amenities(Arrays.asList(
                    Neighborhood.Amenity.RESTAURANTS,
                    Neighborhood.Amenity.COFFEE_SHOPS,
                    Neighborhood.Amenity.LIBRARIES,
                    Neighborhood.Amenity.BARS
                ))
                .transportationOptions(Arrays.asList(
                    Neighborhood.TransportationOption.SUBWAY,
                    Neighborhood.TransportationOption.BUS,
                    Neighborhood.TransportationOption.WALKING_TRAILS
                ))
                .crimeRate(0.06)
                .safetyScore(8.0)
                .schoolRating(8.8)
                .numberOfSchools(5)
                .unemploymentRate(0.05)
                .commuteTimeMinutes(20.0)
                .airQualityIndex(70.0)
                .walkScore(88.0)
                .bikeScore(75.0)
                .transitScore(85.0)
                .diversityIndex(0.90)
                .numberOfRestaurants(35)
                .numberOfParks(4)
                .numberOfLibraries(3)
                .build(),
            
            // Rural retirement community
            Neighborhood.builder()
                .name("Sunset Valley")
                .city("Phoenix")
                .state("AZ")
                .zipCode("85018")
                .latitude(33.4484)
                .longitude(-112.0740)
                .totalPopulation(12000)
                .medianAge(62.5)
                .medianIncome(75000.0)
                .homeOwnershipRate(0.85)
                .collegeGraduateRate(0.55)
                .medianHomeValue(380000.0)
                .medianRent(1800.0)
                .vacancyRate(0.02)
                .lifestyleCharacteristics(Arrays.asList(
                    Neighborhood.LifestyleCharacteristic.SUBURBAN,
                    Neighborhood.LifestyleCharacteristic.RETIREMENT_COMMUNITY
                ))
                .amenities(Arrays.asList(
                    Neighborhood.Amenity.HOSPITALS,
                    Neighborhood.Amenity.PARKS,
                    Neighborhood.Amenity.LIBRARIES,
                    Neighborhood.Amenity.GROCERY_STORES
                ))
                .transportationOptions(Arrays.asList(
                    Neighborhood.TransportationOption.BUS,
                    Neighborhood.TransportationOption.PARKING
                ))
                .crimeRate(0.02)
                .safetyScore(9.5)
                .schoolRating(7.5)
                .numberOfSchools(3)
                .unemploymentRate(0.02)
                .commuteTimeMinutes(45.0)
                .airQualityIndex(80.0)
                .walkScore(35.0)
                .bikeScore(40.0)
                .transitScore(30.0)
                .diversityIndex(0.60)
                .numberOfRestaurants(15)
                .numberOfParks(8)
                .numberOfLibraries(1)
                .build()
        );
        
        neighborhoodRepository.saveAll(neighborhoods);
        log.info("Initialized {} sample neighborhoods", neighborhoods.size());
    }
    
    private void initializeUsers() {
        if (userRepository.count() > 0) {
            log.info("Users already exist, skipping initialization");
            return;
        }
        
        List<User> users = Arrays.asList(
            // Young professional
            User.builder()
                .name("Sarah Johnson")
                .email("sarah.johnson@email.com")
                .age(28)
                .gender(User.Gender.FEMALE)
                .maritalStatus(User.MaritalStatus.SINGLE)
                .educationLevel(User.EducationLevel.MASTERS)
                .incomeLevel(User.IncomeLevel.HIGH)
                .occupationType(User.OccupationType.TECHNOLOGY)
                .lifestylePreferences(Arrays.asList(
                    User.LifestylePreference.URBAN,
                    User.LifestylePreference.YOUNG_PROFESSIONAL
                ))
                .hobbies(Arrays.asList(
                    User.Hobby.FITNESS,
                    User.Hobby.TRAVEL,
                    User.Hobby.MUSIC
                ))
                .familyStatus(User.FamilyStatus.SINGLE)
                .petPreference(User.PetPreference.DOGS)
                .transportationPreference(User.TransportationPreference.PUBLIC_TRANSIT)
                .preferredLocationType(User.LocationType.CITY_CENTER)
                .maxCommuteTimeMinutes(30)
                .maxDistanceMiles(25)
                .minBudget(300000)
                .maxBudget(600000)
                .build(),
            
            // Family with children
            User.builder()
                .name("Michael Chen")
                .email("michael.chen@email.com")
                .age(35)
                .gender(User.Gender.MALE)
                .maritalStatus(User.MaritalStatus.MARRIED)
                .educationLevel(User.EducationLevel.BACHELORS)
                .incomeLevel(User.IncomeLevel.MEDIUM)
                .occupationType(User.OccupationType.HEALTHCARE)
                .lifestylePreferences(Arrays.asList(
                    User.LifestylePreference.SUBURBAN,
                    User.LifestylePreference.FAMILY_ORIENTED
                ))
                .hobbies(Arrays.asList(
                    User.Hobby.SPORTS,
                    User.Hobby.GARDENING,
                    User.Hobby.COOKING
                ))
                .familyStatus(User.FamilyStatus.WITH_CHILDREN)
                .petPreference(User.PetPreference.ANY_PETS)
                .transportationPreference(User.TransportationPreference.CAR)
                .preferredLocationType(User.LocationType.SUBURB)
                .maxCommuteTimeMinutes(45)
                .maxDistanceMiles(40)
                .minBudget(250000)
                .maxBudget(500000)
                .build(),
            
            // Retiree
            User.builder()
                .name("Robert Wilson")
                .email("robert.wilson@email.com")
                .age(68)
                .gender(User.Gender.MALE)
                .maritalStatus(User.MaritalStatus.WIDOWED)
                .educationLevel(User.EducationLevel.BACHELORS)
                .incomeLevel(User.IncomeLevel.MEDIUM)
                .occupationType(User.OccupationType.OTHER)
                .lifestylePreferences(Arrays.asList(
                    User.LifestylePreference.QUIET,
                    User.LifestylePreference.RETIREMENT
                ))
                .hobbies(Arrays.asList(
                    User.Hobby.READING,
                    User.Hobby.GARDENING,
                    User.Hobby.PHOTOGRAPHY
                ))
                .familyStatus(User.FamilyStatus.EMPTY_NESTER)
                .petPreference(User.PetPreference.CATS)
                .transportationPreference(User.TransportationPreference.CAR)
                .preferredLocationType(User.LocationType.SUBURB)
                .maxCommuteTimeMinutes(60)
                .maxDistanceMiles(50)
                .minBudget(200000)
                .maxBudget(400000)
                .build(),
            
            // Graduate student
            User.builder()
                .name("Emily Rodriguez")
                .email("emily.rodriguez@email.com")
                .age(23)
                .gender(User.Gender.FEMALE)
                .maritalStatus(User.MaritalStatus.SINGLE)
                .educationLevel(User.EducationLevel.MASTERS)
                .incomeLevel(User.IncomeLevel.LOW)
                .occupationType(User.OccupationType.EDUCATION)
                .lifestylePreferences(Arrays.asList(
                    User.LifestylePreference.URBAN,
                    User.LifestylePreference.YOUNG_PROFESSIONAL
                ))
                .hobbies(Arrays.asList(
                    User.Hobby.ART,
                    User.Hobby.MUSIC,
                    User.Hobby.READING
                ))
                .familyStatus(User.FamilyStatus.SINGLE)
                .petPreference(User.PetPreference.NO_PETS)
                .transportationPreference(User.TransportationPreference.WALKING)
                .preferredLocationType(User.LocationType.UNIVERSITY_AREA)
                .maxCommuteTimeMinutes(25)
                .maxDistanceMiles(15)
                .minBudget(150000)
                .maxBudget(300000)
                .build()
        );
        
        userRepository.saveAll(users);
        log.info("Initialized {} sample users", users.size());
    }
} 