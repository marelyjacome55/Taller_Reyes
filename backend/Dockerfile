FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /workspace

# Copy maven files and source
COPY pom.xml ./
COPY src ./src

# Build the application (skip tests to speed up)
RUN mvn -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /workspace/target/cotizaciones-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
