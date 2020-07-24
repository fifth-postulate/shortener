FROM openjdk:11.0.6-jre-slim-buster

ADD build/libs/shortener-latest.jar app.jar

RUN adduser --system java

EXPOSE 8080

USER java

ENTRYPOINT [ "sh", "-c", "java -jar /app.jar" ]