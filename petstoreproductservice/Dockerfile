#
# Build stage
#
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /build
COPY . /build/petstoreproductservice
RUN mv /build/petstoreproductservice/pom_root.xml /build/pom.xml
RUN mvn clean install

#
# Package stage
#
FROM openjdk:17-slim
COPY --from=build /build/petstoreproductservice/target/*.jar /app/petstoreproductservice.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/petstoreproductservice.jar"]
