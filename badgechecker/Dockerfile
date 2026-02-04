####################################
# STAGE 1 — BUILD
####################################
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY badgechecker/pom.xml .
RUN mvn dependency:go-offline

COPY badgechecker/src ./src
RUN mvn clean package -DskipTests


####################################
# STAGE 2 — RUNTIME (SELENIUM)
####################################
FROM selenium/standalone-firefox:latest

USER root
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
