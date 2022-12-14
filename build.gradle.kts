import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import io.spring.gradle.dependencymanagement.dsl.ImportsHandler

plugins {
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    java
    `maven-publish`
    jacoco
    id("nebula.integtest") version "9.6.2"
    id("com.dorongold.task-tree") version "2.1.0"
    id("com.google.cloud.tools.jib") version "3.3.1"
}

group = "com.social.network"
version = "0.0.2"
description = "users"
java.sourceCompatibility = JavaVersion.VERSION_17

// version variables
val springVersion = "2.7.5"
val lombokVersion = "1.18.24"
val openApiVersion = "1.6.14"
val postgresqlVersion = "42.5.1"
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
    implementation("org.liquibase:liquibase-core:4.18.0")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("org.springframework.boot:spring-boot-starter-actuator:${springVersion}")
    implementation("io.micrometer:micrometer-registry-prometheus:1.10.2")
    runtimeOnly("org.postgresql:postgresql:${postgresqlVersion}")
    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    testAnnotationProcessor("org.projectlombok:lombok:${lombokVersion}")

    // unit test own dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test:${springVersion}")
    testImplementation("org.mockito:mockito-inline:4.10.0")
    testImplementation("com.h2database:h2:${h2Version}")
    testImplementation("org.testcontainers:junit-jupiter")
    testCompileOnly("org.projectlombok:lombok:${lombokVersion}")

    // integration test own dependencies
    integTestImplementation("org.springframework.cloud:spring-cloud-contract-wiremock:4.0.0")
    integTestImplementation("org.testcontainers:postgresql")
    integTestRuntimeOnly("com.h2database:h2")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

configure<DependencyManagementExtension> {
    imports(delegateClosureOf<ImportsHandler> {
        mavenBom("org.testcontainers:testcontainers-bom:1.17.6")
    })
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

tasks.integrationTest {
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
