FROM openjdk:12-jdk-alpine
LABEL maintainer="mario-dacosta@hotmail.fr"
EXPOSE 8080
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} dilemma-geolocation.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/dilemma-geolocation.jar"]