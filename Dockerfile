# Use a lightweight JDK base image
FROM eclipse-temurin:22-jre-alpine

# Set working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY target/task-api-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the Spring Boot app runs on
EXPOSE 8080

# Environment variable for MongoDB connection
# This will be overridden in Kubernetes or Docker run
ENV MONGODB_URI=mongodb://host.docker.internal:27017/kaiburr

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
