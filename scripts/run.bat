@echo off
REM Student Data Analysis System - Run Script (Windows)
REM This script builds and runs the application

echo ============================================================
echo   STUDENT DATA ANALYSIS SYSTEM
echo   Starting Application...
echo ============================================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 11 or higher
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)

REM Display Java version
echo Java version:
java -version
echo.

REM Build the project
echo Building project...
call mvn clean package -DskipTests

if errorlevel 1 (
    echo.
    echo ERROR: Build failed
    echo Please check the error messages above
    pause
    exit /b 1
)

echo.
echo Build successful!
echo.

REM Run the application
echo Running application...
for %%F in (target\*-with-dependencies.jar) do (
    set JAR_FILE=%%F
)

if not defined JAR_FILE (
    echo ERROR: JAR file not found in target directory
    pause
    exit /b 1
)

echo JAR file: %JAR_FILE%
echo.

java -jar %JAR_FILE%

echo.
echo Application terminated
pause
