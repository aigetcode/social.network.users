FROM openjdk:17

RUN mkdir /app
COPY users.jar /app
WORKDIR /app
EXPOSE 8080

ENV SPRING_DATASOURCE_URL="jdbc:postgresql://postgresdb-container:5432/users"
ENV SPRING_DATASOURCE_USERNAME="postgres"
ENV SPRING_DATASOURCE_PASSWORD="postgres"

CMD java -jar users.jar
