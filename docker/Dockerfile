FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/coursemanagement.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
