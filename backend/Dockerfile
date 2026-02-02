# Correct Java 21 base image
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy Maven wrapper & pom first (better caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Give execute permission & download dependencies
RUN chmod +x mvnw && ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose Spring Boot port
EXPOSE 8080

# Run the JAR
CMD ["java", "-jar", "target/HotelManagementSystem-0.0.1-SNAPSHOT.jar"]
