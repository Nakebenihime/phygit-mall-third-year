FROM openjdk:12-jdk-alpine
LABEL maintainer="mario-dacosta@hotmail.fr"
EXPOSE 8686
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} dilemma-frequentation.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/dilemma-frequentation.jar"]