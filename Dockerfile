# Multi-stage Dockerfile for Java 21+ JavaFX Application

# Stage 1: Build stage with Maven
FROM maven:3.9-eclipse-temurin-21 as builder

WORKDIR /build

# Copy project files
COPY sistema-orcamento/ .

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Install additional dependencies for JavaFX and X11 support (optional)
RUN apt-get update && apt-get install -y \
    libx11-dev \
    libxext-dev \
    libxrender-dev \
    libfreetype6 \
    fontconfig \
    ttf-dejavu-core \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copy the built JAR from builder stage
COPY --from=builder /build/target/*.jar application.jar

# Expose port for remote debugging (optional)
EXPOSE 5005 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/health || exit 1

# Set UTF-8 environment
ENV LANG=C.UTF-8 \
    LANGUAGE=en_US:en \
    LC_ALL=C.UTF-8 \
    DB_HOST=mysql \
    DB_PORT=3306 \
    DB_NAME=orcamento_db \
    DB_USER=orcamento_user \
    DB_PASSWORD=orcamento_pass

# Run the application
ENTRYPOINT ["java", \
    "-XX:+UseG1GC", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.awt.headless=false", \
    "-Dfile.encoding=UTF-8", \
    "-jar", "application.jar"]
