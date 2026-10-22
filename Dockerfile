# Stage 1: Build Spring Boot Application
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Download dependencies first for better build caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build executable JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run Application
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create a non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Create uploads directory and set permissions
RUN mkdir -p /app/uploads \
    && chown -R appuser:appgroup /app

# Copy application JAR
COPY --from=builder --chown=appuser:appgroup /app/target/*.jar app.jar

USER appuser

# Render provides PORT; use 8080 locally if PORT is absent
ENV PORT=8080
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar"]
