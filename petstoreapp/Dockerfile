#
# Build stage
#
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /build
COPY . /build/petstoreapp
RUN mv /build/petstoreapp/pom_root.xml /build/pom.xml
RUN sed -i 's/localhost/host.docker.internal/g' /build/petstoreapp/src/main/resources/application.yml
RUN mvn clean install

#
# Package stage
#
FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu
RUN apt-get update -y && apt-get install -y --no-install-recommends openssh-server 
RUN mkdir -p /run/sshd && echo "root:Docker!" | chpasswd
COPY sshd_config /etc/ssh/ 
EXPOSE 2222 8080
COPY --from=build /build/petstoreapp/target/*.jar /app/petstoreapp.jar
ENTRYPOINT ["/bin/bash", "-c", "/usr/sbin/sshd && java -jar /app/petstoreapp.jar"]
