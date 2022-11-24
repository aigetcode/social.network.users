plugins {
    id("org.springframework.boot") version "2.7.5"
    java
    `maven-publish`
    jacoco
}

group = "com.social.network"
version = "0.0.1"
description = "users"
java.sourceCompatibility = JavaVersion.VERSION_17

// version variables
val springVersion = "2.7.5"
val lombokVersion = "1.18.24"
val openApiVersion = "1.6.12"
val postgresqlVersion = "42.5.0"
val h2Version = "2.1.214"

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${springVersion}")
    implementation("org.springframework.boot:spring-boot-starter-validation:${springVersion}")
    implementation("org.springframework.boot:spring-boot-starter-web:${springVersion}")
    implementation("org.springdoc:springdoc-openapi-ui:${openApiVersion}")
    implementation("commons-validator:commons-validator:1.7")
    implementation("org.liquibase:liquibase-core:4.17.2")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    runtimeOnly("org.postgresql:postgresql:${postgresqlVersion}")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${springVersion}")
    testImplementation("org.mockito:mockito-inline:4.8.0")
    testImplementation("com.h2database:h2:${h2Version}")

    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    testCompileOnly("org.projectlombok:lombok:${lombokVersion}")
    testAnnotationProcessor("org.projectlombok:lombok:${lombokVersion}")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.8".toBigDecimal()
            }

            includes = listOf(
                "com.social.network.users.endpoint.*",
                "com.social.network.users.service.*",
            )
        }
    }
}
