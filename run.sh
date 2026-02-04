#!/bin/bash

echo "========================================="
echo "Codecademy Badge Checker - Local Run"
echo "========================================="
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null
then
    echo "❌ Maven is not installed. Please install Maven first."
    echo "Visit: https://maven.apache.org/install.html"
    exit 1
fi

# Check Java version
echo "Checking Java version..."
java -version

echo ""
echo "Building and running the application..."
echo ""

# Clean and run
mvn clean spring-boot:run

echo ""
echo "Application stopped."