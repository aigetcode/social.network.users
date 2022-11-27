# User service
This service handles basics operation with users and followers


### Check test coverage (minimum 80%):
    gradle build jacoco jacocoTestCoverageVerification

### Build jacoco report:
    gradle build jacoco jacocoTestReport

Reports will build in `build/reports/test/html/index.html`

### Grafana:

    localhost:3000

## Docker compose

#### 0. Build gradle project
    gradle clean build

#### 1. Build service image

    ./servis.sh build

#### 2. Start containers

    ./servis.sh start

#### 2. Check that all containers are alive

    docker ps -a

#### 3. Stop containers

    ./server.sh stop

#### 4. If you need to install from scratch - i.e. delete all data, then we perform a cleaning

    ./server.sh clean


## Tests

#### Unit tests 
    gradle test

#### Integration tests
    gradle integrationTest
