#
# Build stage
#
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /build
COPY . /build/petstoreorderservice
RUN mv /build/petstoreorderservice/pom_root.xml /build/pom.xml
RUN sed -i 's/localhost/host.docker.internal/g' /build/petstoreorderservice/src/main/resources/application.yml
RUN mvn clean install

#
# Package stage
#
FROM openjdk:17-slim
COPY --from=build /build/petstoreorderservice/target/*.jar /app/petstoreorderservice.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","/app/petstoreorderservice.jar"]
