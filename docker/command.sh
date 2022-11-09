#!/bin/bash

# create image postgres-db
docker build -t postgres-db ./postgres/

# run container from image
docker run -d --name postgresdb-container -p 5001:5432 -v /E/docs/dockerDbVolume:/var/lib/postgresql/data postgres-db

# create image pgadmin
docker build -t pgadmin ./pgadmin/

# run container from image
docker run -d --name pgadmin-container -p 9000:80 pgadmin

# create network
docker network create db-net
docker network connect db-net postgresdb-container
docker network connect db-net pgadmin-container
