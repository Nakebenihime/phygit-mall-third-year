FROM openjdk:12-jdk-alpine
LABEL maintainer="mario-dacosta@hotmail.fr"
EXPOSE 8484
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} authentication.jar
ADD keystore.p12 /keystore.p12
# RUN keytool -genkeypair -alias RSA -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore /keystore.p12 -storepass zvyxT6M04BDgp8 -validity 3650 -dname "CN=Mark Smith, OU=Java, O=Sun, L=Cupertino, S=California, C=US"
# RUN keytool -export -alias RSA -keystore keystore.p12 -storepass zvyxT6M04BDgp8 -file certificate -rfc
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/authentication.jar","/keystore.p12","zvyxT6M04BDgp8","PKCS12", "RSA", "5n5LiCFNJCuXfk"]
