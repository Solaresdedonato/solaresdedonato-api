# Multi-stage: Railway (y cualquier PaaS que buildee desde el repo, no desde un jar ya
# compilado) necesita poder construir la imagen sin un paso manual de `mvnw package` antes.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q -DskipTests package

FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="gastoongimenez43@gmail.com"
LABEL application="solares-donato-api"

RUN addgroup -S spring && adduser -S spring -G spring

# El volumen persistente se monta en /data (Railway: Volumes -> mount path /data).
# mkdir + chown ANTES del USER spring: un volumen recién creado se monta como root:root,
# y sin esto el primer Files.createDirectories() de LocalFileStorageAdapter tira
# AccessDeniedException — la causa #1 de "andaba local y en prod no".
RUN mkdir -p /data/uploads && chown -R spring:spring /data

USER spring:spring

WORKDIR /app

COPY --from=build --chown=spring:spring /build/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", "app.jar"]
