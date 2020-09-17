FROM openjdk:12-jdk-alpine
LABEL maintainer="mario-dacosta@hotmail.fr"
EXPOSE 8181
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} dilemma-journey.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/dilemma-journey.jar"]