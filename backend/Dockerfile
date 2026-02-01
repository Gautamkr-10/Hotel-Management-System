# Use a Java 21 base image
FROM eclipse-temurin:21-jdk-focal

WORKDIR /app

# Copy project files
COPY . .

# Build project
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/HotelManagementSystem-0.0.1-SNAPSHOT.jar"]
