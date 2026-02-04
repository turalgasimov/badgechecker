####################################
# STAGE 1 — BUILD WITH MAVEN
####################################
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests


####################################
# STAGE 2 — RUNTIME WITH FIREFOX
####################################
FROM eclipse-temurin:17-jre

# Install Firefox + dependencies
RUN apt-get update && apt-get install -y \
    firefox-esr \
    wget \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Install GeckoDriver
RUN wget -O /tmp/geckodriver.tar.gz \
    https://github.com/mozilla/geckodriver/releases/download/v0.34.0/geckodriver-v0.34.0-linux64.tar.gz \
    && tar -xzf /tmp/geckodriver.tar.gz -C /usr/local/bin \
    && chmod +x /usr/local/bin/geckodriver \
    && rm /tmp/geckodriver.tar.gz

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
