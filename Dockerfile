# Imagen base con JDK 17 (compatible con Spring Boot 3.4)
FROM eclipse-temurin:17-jdk

# Directorio de trabajo
WORKDIR /app

# Copiar archivos de Maven Wrapper
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

# Copiar pom.xml primero (cache de dependencias)
COPY pom.xml .

# Descargar dependencias sin compilar (mejor cache)
RUN ./mvnw dependency:go-offline

# Copiar el resto del código
COPY src src

# Construir .jar
RUN ./mvnw -q -DskipTests package

# Exponer el puerto donde corre Spring Boot (estándar)
EXPOSE 8080

# Ejecutar la app (Render usará el puerto $PORT)
CMD ["sh", "-c", "java -jar target/*.jar --server.port=$PORT"]
