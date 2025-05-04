FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY gradle ./gradle
COPY build.gradle settings.gradle gradlew ./
RUN chmod +x gradlew

RUN ./gradlew dependencies

COPY src ./src

RUN ./gradlew clean build -x test


FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Add wait-for-it.sh
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]