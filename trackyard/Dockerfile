# syntax=docker/dockerfile:1

## Build stage
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Cache dependencies first
COPY pom.xml .
RUN mvn -ntp -B -DskipTests dependency:go-offline

# Copy sources and build
COPY src ./src
RUN mvn -ntp -B -DskipTests package \
    && cp target/*-SNAPSHOT.jar app.jar

## Runtime stage
FROM eclipse-temurin:17-jre-alpine

ENV TZ=UTC \
    JAVA_OPTS="-XX:MaxRAMPercentage=75.0"

WORKDIR /app
COPY --from=build /app/app.jar /app/app.jar

# Spring Boot defaults to 8080; configure Render to expose this port
EXPOSE 8080

# Use sh -c to allow JAVA_OPTS env expansion
CMD ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar"]

