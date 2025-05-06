FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy the gradle wrapper and config files
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle

# Make gradlew executable
RUN chmod +x gradlew

# Pre-download dependencies to cache them
RUN ./gradlew --no-daemon dependencies

# Copy the source code
COPY src ./src

# Build the application
RUN ./gradlew --no-daemon clean build -x test


FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]