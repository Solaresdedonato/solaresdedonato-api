#!/bin/sh
set -e

# Railway monta el volumen persistente en /data EN RUNTIME, después de que la imagen ya
# se construyó -- pisa cualquier permiso seteado en el Dockerfile con un RUN chown. Un
# volumen recién creado (o cada vez que Railway lo remonta) queda root:root, así que el
# chown tiene que pasar ACA, en cada arranque de contenedor, mientras todavía somos root,
# antes de bajar privilegios al usuario spring para correr la app.
if [ -d /data ]; then
  chown -R spring:spring /data
fi

exec su-exec spring:spring java \
    -Djava.security.egd=file:/dev/./urandom \
    -XX:MaxRAMPercentage=75.0 \
    -jar app.jar
