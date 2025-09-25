#!/bin/bash
# Explicit build script for Railway deployment
# This ensures Maven is used instead of Gradle

echo "Starting Maven build for Spring Boot application..."
echo "Working directory: $(pwd)"
echo "Contents: $(ls -la)"

# Clean and build with Maven
mvn clean package -DskipTests

echo "Build completed. JAR file location:"
ls -la target/*.jar

echo "Build script finished successfully."