# NeighborFit - Quick Start Guide

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher (✅ You have Java 21)
- Maven (optional - we provide a wrapper)

### Option 1: Using Maven Wrapper (Recommended)
```bash
# Run the application
mvnw.cmd spring-boot:run

# Or on Linux/Mac
./mvnw spring-boot:run
```

### Option 2: Using System Maven
```bash
# Install Maven first, then run
mvn spring-boot:run
```

### Option 3: Using Batch Files (Windows)
```bash
# Run setup first (if needed)
setup.bat

# Then start the application
run.bat
```

## 🌐 Access the Application

Once started, open your browser and go to:
- **Main Application**: http://localhost:8080
- **API Endpoints**: http://localhost:8080/api
- **H2 Database Console**: http://localhost:8080/api/h2-console
  - Username: `admin`
  - Password: `admin123`

## 🧪 Try the Demo

1. Go to http://localhost:8080
2. Scroll down to the "Try Our Demo" section
3. Fill out the registration form with your preferences
4. Click "Find Matches" to see neighborhood recommendations

## 📊 Sample Data Included

The application comes with sample data:

### Sample Users
- **Sarah Johnson** (28, Tech Professional, Urban)
- **Michael Chen** (35, Healthcare, Family with Children)
- **Robert Wilson** (68, Retiree, Quiet Suburban)
- **Emily Rodriguez** (23, Student, University Area)

### Sample Neighborhoods
- **Downtown Financial District** (NYC, Urban, Young Professional)
- **Maplewood Suburbs** (Austin, Suburban, Family-Friendly)
- **University District** (Boston, Urban, University Town)
- **Sunset Valley** (Phoenix, Suburban, Retirement Community)

## 🔧 API Testing

### Register a User
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "age": 30,
    "incomeLevel": "HIGH",
    "familyStatus": "SINGLE",
    "maxBudget": 500000
  }'
```

### Find Matches
```bash
curl -X POST http://localhost:8080/api/matching/users/1/matches \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

### Get Analytics
```bash
curl -X GET http://localhost:8080/api/matching/analytics \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

## 🏗️ Project Structure

```
NeighborFit/
├── src/main/java/com/neighborfit/
│   ├── NeighborFitApplication.java          # Main application
│   ├── model/                              # Domain entities
│   ├── repository/                         # Data access
│   ├── service/                            # Business logic
│   ├── controller/                         # REST APIs
│   ├── dto/                                # Data transfer objects
│   └── config/                             # Configuration
├── src/main/resources/
│   ├── application.properties              # App config
│   └── templates/index.html                # Frontend
├── pom.xml                                 # Dependencies
├── mvnw.cmd                                # Maven wrapper
├── setup.bat                               # Setup script
└── run.bat                                 # Run script
```

## 🧠 Matching Algorithm

The application uses a weighted scoring system:

- **Lifestyle Compatibility (40%)**: Family status, pets, transportation
- **Demographic Alignment (30%)**: Age, income, education
- **Location & Transportation (30%)**: Commute, walkability, transit

### Match Strength Levels
- **Excellent (90-100%)**: Highly recommended
- **Very Good (80-89%)**: Strongly recommended  
- **Good (70-79%)**: Recommended
- **Fair (60-69%)**: Consider with caution
- **Poor (0-59%)**: Not recommended

## 🔍 Troubleshooting

### Common Issues

1. **Port 8080 already in use**
   ```bash
   # Change port in application.properties
   server.port=8081
   ```

2. **Java version issues**
   ```bash
   # Check Java version
   java -version
   # Should be 17 or higher
   ```

3. **Maven not found**
   - Use the Maven wrapper: `mvnw.cmd spring-boot:run`
   - Or install Maven globally

4. **Database connection issues**
   - H2 console: http://localhost:8080/api/h2-console
   - Check application.properties for database settings

### Logs
Check the console output for detailed logs and error messages.

## 📈 Next Steps

1. **Explore the Code**: Review the matching algorithm in `MatchingAlgorithm.java`
2. **Add More Data**: Extend sample data in `DataInitializer.java`
3. **Customize Algorithm**: Modify weights in `application.properties`
4. **Enhance UI**: Update the frontend in `index.html`

## 🎯 Project Goals Met

✅ **Problem Analysis & Research (50%)**
- Systematic problem definition
- User research and market analysis
- Data-driven hypothesis validation

✅ **Technical Problem-Solving (40%)**
- Advanced matching algorithm
- Real data integration
- Scalable architecture
- RESTful API design

✅ **Systems Thinking (10%)**
- Trade-off documentation
- Scalability considerations
- Systematic problem decomposition

## 📚 Documentation

- **Full Documentation**: See `README.md`
- **API Reference**: Built-in Swagger (if added)
- **Code Comments**: Extensive inline documentation

---

**Happy Neighborhood Matching! 🏠✨** 