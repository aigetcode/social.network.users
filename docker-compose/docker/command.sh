#!/bin/bash

# create image postgres-db
docker build -t postgres-db:0.0.1 ./postgres/

# run container from image
docker run -d --name postgresdb-container -p 5001:5432 -v /E/docs/dockerDbVolume:/var/lib/postgresql/data postgres-db:0.0.1

# create image pgadmin
docker build -t pgadmin:0.0.1 ./pgadmin/

# run container from image
docker run -d --name pgadmin-container -p 9000:80 pgadmin:0.0.1

# create docker image from service
docker build -t users:0.0.1 ./service/

# run service container from image
docker run  -d --name users -p 8008:8080 users:0.0.1

# create network
docker network create db-net
docker network connect db-net postgresdb-container
docker network connect db-net pgadmin-container
docker network connect db-net users
