# Use a base image with Java runtime
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built jar from the Maven target directory
COPY target/*.jar app.jar

# Expose a port if your app listens on one (adjust as needed)
EXPOSE 8090

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
