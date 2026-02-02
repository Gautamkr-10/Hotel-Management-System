FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

EXPOSE 4040

CMD ["java", "-jar", "target/HotelManagementSystem-0.0.1-SNAPSHOT.jar"]
