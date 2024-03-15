FROM jelastic/maven:3.9.5-openjdk-21 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
COPY --from=build /target/demo2-0.0.1-SNAPSHOT.jar demo2.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo2.jar"]