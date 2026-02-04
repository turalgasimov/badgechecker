####################################
# STAGE 1 — BUILD
####################################
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY badgechecker/pom.xml ./pom.xml
COPY badgechecker/src ./src

RUN mvn clean package -DskipTests

####################################
# STAGE 2 — RUNTIME (SELENIUM)
####################################
FROM selenium/standalone-firefox:latest

USER root
WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Set the port Railway expects
ENV PORT=8080
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
