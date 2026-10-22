# ==========================================
# Stage 1: Build the Application
# ==========================================
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copy pom.xml and download dependencies (layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy project source code and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# Stage 2: Runtime Environment
# ==========================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create a non-root user and group for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Create directory for file uploads
RUN mkdir -p /app/uploads && chown -R appuser:appgroup /app

# Copy the built jar from the builder stage
COPY --from=builder --chown=appuser:appgroup /app/target/*.jar app.jar

# Switch to non-root user
USER appuser

# Expose default Spring Boot port
EXPOSE 8080

# Environment variables with sensible defaults
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
