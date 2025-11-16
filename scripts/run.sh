#!/bin/bash

# Student Data Analysis System - Run Script
# This script builds and runs the application

echo "============================================================"
echo "  STUDENT DATA ANALYSIS SYSTEM"
echo "  Starting Application..."
echo "============================================================"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java 11 or higher"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    echo "Please install Maven 3.6 or higher"
    exit 1
fi

# Display Java version
echo "Java version:"
java -version
echo ""

# Build the project
echo "Building project..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Build failed"
    echo "Please check the error messages above"
    exit 1
fi

echo ""
echo "Build successful!"
echo ""

# Find the JAR file
JAR_FILE=$(find target -name "*-with-dependencies.jar" | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "ERROR: JAR file not found in target directory"
    exit 1
fi

echo "Running application..."
echo "JAR file: $JAR_FILE"
echo ""

# Run the application
java -jar "$JAR_FILE"

echo ""
echo "Application terminated"
