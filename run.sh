#!/usr/bin/env bash
./mvnw com.dkanejs.maven.plugins:docker-compose-maven-plugin:4.0.0:up && ./mvnw spring-boot:run
