# Build
FROM maven:3.6-jdk-8 AS build
WORKDIR /app
COPY src src
COPY pom.xml pom.xml
RUN mvn -DskipTests -f /app/pom.xml clean package
# Package
FROM openjdk:8
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]

#sudo chmod +x mvnw