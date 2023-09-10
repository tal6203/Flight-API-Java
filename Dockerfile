# Use the official Maven image as the build stage
FROM maven:3.8.2-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the Maven project file
COPY pom.xml .

# Download the Maven dependencies (this step can be cached if the pom.xml hasn't changed)
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build the application without running tests
RUN mvn package -DskipTests

# Use the official OpenJDK image for the final stage
FROM openjdk:17

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage to this stage
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar demo.jar

# Expose the port (if needed)
# EXPOSE 8080

# Define the command to run your application
CMD ["java", "-jar", "demo.jar"]
