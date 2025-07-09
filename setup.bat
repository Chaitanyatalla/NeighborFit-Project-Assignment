@echo off
echo NeighborFit Setup Script
echo =======================

echo Checking Java installation...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

echo.
echo Checking Maven installation...
mvn -version
if %errorlevel% neq 0 (
    echo Maven is not installed or not in PATH
    echo.
    echo Installing Maven...
    echo Please download Maven from: https://maven.apache.org/download.cgi
    echo Extract to C:\Program Files\Apache\maven
    echo Add C:\Program Files\Apache\maven\bin to your PATH environment variable
    echo.
    echo Alternative: Use the Maven wrapper (mvnw.cmd) included in this project
    echo.
    pause
    exit /b 1
)

echo.
echo Building NeighborFit application...
mvn clean install

if %errorlevel% equ 0 (
    echo.
    echo Build successful!
    echo.
    echo To run the application:
    echo   mvn spring-boot:run
    echo.
    echo Then open: http://localhost:8080
    echo.
) else (
    echo.
    echo Build failed! Please check the error messages above.
    echo.
)

pause 