FROM postgres
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD 123
ENV POSTGRES_DB users
COPY users.sql /docker-entrypoint-initdb.d/
