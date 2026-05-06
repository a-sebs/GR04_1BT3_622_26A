# Imagen base ligera compatible con Java 21
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
# Toma el WAR generado por Maven
ARG WAR_FILE=target/*.war
COPY ${WAR_FILE} app.war
EXPOSE 8080
# Ejecuta la aplicación
ENTRYPOINT ["java", "-jar", "/app.war"]