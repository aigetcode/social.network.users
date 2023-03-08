FROM openjdk:17

RUN mkdir /app
COPY users.jar /app
WORKDIR /app
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "users.jar"]
