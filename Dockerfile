# Stage 1: Build the application (JDK used to compile and run Gradle)
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy Gradle wrapper and config
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Ensure gradlew is executable
RUN chmod +x ./gradlew

# Copy source
COPY src src

# Build the executable JAR (skip tests to speed up)
RUN ./gradlew bootJar -x test --no-daemon

# Stage 2: Run-time image (JRE only for smaller final image)
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the fat/spring-boot jar from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
