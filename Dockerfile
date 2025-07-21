FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/api_gateway-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080