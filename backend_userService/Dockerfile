FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/backend_userService.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
