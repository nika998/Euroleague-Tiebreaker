FROM maven:3.9.5-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk AS runtime

WORKDIR /app

COPY --from=build /app/target/tiebreaker-0.0.1-SNAPSHOT.jar tiebreaker-app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=default

CMD ["java", "-jar", "tiebreaker-app.jar"]
