FROM openjdk:8-jdk-alpine

RUN mkdir -p app
ADD app.war ./app
WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java","-jar","./app.war"]
