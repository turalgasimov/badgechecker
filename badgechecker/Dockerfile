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

apt-get update && apt-get install -y \
    firefox-esr \
    xvfb \
    libgtk-3-0 \
    libdbus-glib-1-2 \
    libx11-xcb1 \
    libasound2

FROM selenium/standalone-firefox:latest

USER root
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
