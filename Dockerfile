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

# su-exec: para bajar privilegios a spring en el entrypoint DESPUÉS de poder
# chownear el volumen (ver docker-entrypoint.sh) — no se puede hacer con USER acá,
# ver comentario abajo.
RUN addgroup -S spring && adduser -S spring -G spring \
    && apk add --no-cache su-exec

WORKDIR /app

COPY --from=build --chown=spring:spring /build/target/*.jar app.jar
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

EXPOSE 8081

# Sin USER spring:spring acá a propósito: Railway monta el volumen persistente en
# /data EN RUNTIME (después del build), siempre root:root si es nuevo o si Railway
# lo remonta — un chown en el Dockerfile (build time) no le llega, se pisa. El
# contenedor arranca como root para poder chownearlo en cada boot; docker-entrypoint.sh
# baja a spring recién después, vía su-exec, para correr la app sin privilegios de root.
ENTRYPOINT ["/docker-entrypoint.sh"]
