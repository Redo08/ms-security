
# Utiliza una imagen de Maven con JDK 17 para compilar la aplicación
# maven:3.9-eclipse-temurin-17 incluye Maven 3.9 y OpenJDK 17
FROM maven:3.9-eclipse-temurin-17 AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo pom.xml primero (optimización de caché de Docker)
COPY pom.xml .

# Copia el código fuente de la aplicación
COPY src ./src

# Ejecuta Maven para compilar y empaquetar la aplicación
# Genera un archivo JAR en target/*.jar
RUN mvn clean package -DskipTests

#EJECUCIÓN
# Utiliza una imagen más ligera con solo JRE 17 (sin herramientas de desarrollo)
# alpine: variante minimalista de Linux para reducir el tamaño de la imagen
FROM eclipse-temurin:17-jre-alpine

# Establece el directorio de trabajo para la aplicación
WORKDIR /app

# Copia el JAR compilado desde la etapa de construcción (build)
# --from=build: referencia a la etapa anterior
# Busca cualquier archivo .jar en target/ y lo renombra como app.jar
COPY --from=build /app/target/*.jar app.jar

# Documenta el puerto en el que la aplicación escuchará
# NOTA: Esto es solo documentación, no publica el puerto automáticamente
# Ajusta este valor según la configuración de tu application.properties
EXPOSE 8080

# Comando que se ejecutará cuando el contenedor inicie
# Ejecuta la aplicación Spring Boot usando java -jar
ENTRYPOINT ["java", "-jar", "app.jar"]


# BENEFICIOS DEL MULTI-STAGE BUILD:
# - Imagen final más pequeña (solo contiene JRE y el JAR, no Maven ni código fuente)
#coment