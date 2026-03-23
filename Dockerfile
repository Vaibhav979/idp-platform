FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/idp-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]