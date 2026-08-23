# solares-donato-api

API de Solares de Donato: desarrollos inmobiliarios, biblioteca de contenido multimedia, consultas de contacto y registro de brokers.

## Bootstrap

Este proyecto se generó a partir del template `scaffold-api`. Ver `C:\Users\gaston.gimenez\Documents\claude\beta\scaffold.md` para el runbook de bootstrap (no versionado en este repo).

## Convenciones

Arquitectura hexagonal — ver `C:\Users\gaston.gimenez\Documents\claude\beta\HEXAGONAL.md` (no versionado en este repo).

Diseño de dominio, esquema de BD y listado de endpoints: `..\docs\plan-solares-de-donato.md`.

## Stack

- Java 17
- Spring Boot 3.4.x (Web, Data JPA, Security, Validation, Actuator)
- PostgreSQL + Flyway + TestContainers
- JWT (jjwt 0.11.x)
- Google Drive API v3 (ingesta de fotos y videos — ver "Google Drive" más abajo)
- Springdoc OpenAPI
- Lombok
- Logback
- Micrometer + Prometheus + tracing (Brave/Zipkin, desactivado por `management.tracing.enabled=false`)

## Local

1. `docker compose up -d` — levanta Postgres local (requiere Docker Desktop corriendo).
2. Copiar `local.properties.example` a `local.properties` (gitignoreado) y completar `jwt.secret` y las credenciales de Postgres — es lo que reemplaza a los viejos profiles de Spring y al `setx`. `application.properties` lo importa vía `spring.config.import=optional:file:./local.properties`, así que no hace falta ninguna env var para correr local.
3. `./mvnw spring-boot:run` — arranca en `http://localhost:8081/solares-donato`, actuator en `http://localhost:9090/actuator/health`, Swagger en `http://localhost:8081/solares-donato/swagger-ui.html`.

Las migraciones de Flyway (`src/main/resources/db/migration/`) corren automáticamente al arrancar, incluida la carga de un `usuario_admin` inicial (ver `V2__seed_usuario_admin.sql`).

No hay profiles de Spring (`local`/`test`/`preprod`/`prod`) — es un solo ambiente productivo (Railway) más tu notebook, así que toda la config vive en un único `application.properties` con `${ENV_VAR:default}`: el default sirve a la notebook, y Railway pisa lo que necesite desde su dashboard.

## Google Drive (ingesta de fotos y videos)

Drive es la fuente de ingesta, no el hosting — pero fotos y videos se tratan distinto:

- **Foto**: se baja una vez y se guarda vía el storage local de siempre (`/media/**`).
- **Video**: NUNCA se descarga (no es viable cargar un video entero en memoria acá). Se
  enlaza directo a Drive (`ArchivoExternoPort.construirUrlReproduccion`). **Para que ese
  video se vea en el sitio público, el archivo puntual tiene que estar compartido en
  Drive como "cualquiera con el link"** — el acceso de solo-lectura del service account
  alcanza para importarlo al backoffice, pero no para que lo reproduzca un visitante
  anónimo. Si te olvidás de cambiar ese permiso por video, se importa igual pero se ve
  roto en el sitio.

Sin configurar, `/v1/drive/**` responde un error claro (`DRIVE_NO_CONFIGURADO`) en vez de
tirar abajo el arranque de toda la app — es una integración opcional.

Setup (una sola vez):
1. En GCP, crear un service account y habilitar la Drive API.
2. Descargar su JSON de credenciales.
3. Compartir la carpeta de Drive con el email del service account (`...@...iam.gserviceaccount.com`) como **Lector** — nunca Editor, el scope ya es `drive.readonly`.
4. En `local.properties` (ver `local.properties.example`): `app.drive.credentials-json` con el JSON completo en una sola línea, y `app.drive.folder-id` con el id de la carpeta (de su URL). En Railway van como `GOOGLE_DRIVE_CREDENTIALS_JSON` / `GOOGLE_DRIVE_FOLDER_ID`.

## Comandos

```bash
./mvnw test                    # tests (sin el gate de cobertura)
./mvnw clean verify             # tests + cobertura >= 80% por paquete — hoy NO pasa (repo sin tests
                                 # todavía en la mayoría de los paquetes); usar mvnw test hasta la
                                 # pasada de tests dedicada
./mvnw spring-boot:run         # arrancar local
docker build -t solares-donato-api . # build imagen (multi-stage, no requiere un jar pre-compilado)
```
