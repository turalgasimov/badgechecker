FROM maven:3.9.6-eclipse-temurin-17

# Install Firefox
RUN apt-get update && apt-get install -y firefox-esr wget

# Install GeckoDriver
RUN wget https://github.com/mozilla/geckodriver/releases/download/v0.36.0/geckodriver-v0.36.0-linux-aarch64.tar.gz \
 && tar -xvzf geckodriver-v0.36.0-linux-aarch64.tar.gz \
 && mv geckodriver /usr/local/bin/ \
 && chmod +x /usr/local/bin/geckodriver

WORKDIR /app

COPY . .
RUN mvn clean package -DskipTests

CMD ["java", "-jar", "target/*.jar"]
