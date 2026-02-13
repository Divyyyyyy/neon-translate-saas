# Build stage - Maven + Java 17
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy pom first for better layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# Run stage - only JRE
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create non-root user (optional, good for security)
RUN adduser -D appuser
USER appuser

COPY --from=build /app/target/smart-expense-0.0.1-SNAPSHOT.jar app.jar

# Render sets PORT; your application.properties already uses ${PORT:8080}
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
