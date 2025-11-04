# Use a valid base image for Java 17
FROM openjdk:17-slim

# Set working directory
WORKDIR /app

# Copy the built jar file into the image
COPY target/user-service.jar app.jar

# Expose the port the app listens on
EXPOSE 8081

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
