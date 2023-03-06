#
# Build stage
#
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /build
COPY . /build/petstorepetservice
RUN mv /build/petstorepetservice/pom_root.xml /build/pom.xml
RUN mvn clean install

#
# Package stage
#
FROM openjdk:17-slim
COPY --from=build /build/petstorepetservice/target/*.jar /app/petstorepetservice.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/petstorepetservice.jar"]
