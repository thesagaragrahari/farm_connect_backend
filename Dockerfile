# Use official OpenJDK image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the jar file (replace with your jar name)
COPY target/krishisetu-0.0.1-SNAPSHOT.jar app.jar


# Expose port (same as in application.properties)
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java","-jar","app.jar"]
