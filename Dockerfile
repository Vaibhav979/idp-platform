FROM eclipse-temurin:17-jre-alpine

RUN apk add --no-cache git docker-cli

WORKDIR /app

COPY target/idp-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]