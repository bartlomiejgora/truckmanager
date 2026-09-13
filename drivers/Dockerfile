# Etap 1: Budowanie aplikacji
FROM maven:3.9-eclipse-temurin-25 AS builder
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Etap 2: Środowisko uruchomieniowe
FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8081 9090

ENTRYPOINT ["java", "-jar", "app.jar"]
