FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17
VOLUME /tmp
COPY target/calculator-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]