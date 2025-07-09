@echo off
echo Starting NeighborFit Application...
echo.

echo Checking if Maven is available...
mvn -version >nul 2>&1
if %errorlevel% equ 0 (
    echo Using system Maven...
    mvn spring-boot:run
) else (
    echo Maven not found, using Maven wrapper...
    if exist mvnw.cmd (
        mvnw.cmd spring-boot:run
    ) else (
        echo ERROR: Neither Maven nor Maven wrapper found!
        echo Please install Maven or run setup.bat first.
        pause
        exit /b 1
    )
)

echo.
echo Application started successfully!
echo Open your browser and go to: http://localhost:8080
echo.
pause 