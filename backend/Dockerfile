# Use OpenJDK 17 slim
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy everything from backend folder into container
COPY . .

# Build the Spring Boot project with Maven (skip tests)
RUN ./mvnw clean package -DskipTests

# Expose the port the app will run on
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "target/hotel-management-system-0.0.1-SNAPSHOT.jar"]
