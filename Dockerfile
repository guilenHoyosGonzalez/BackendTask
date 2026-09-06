# =========================
# Etapa 1: Compilar
# =========================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar el proyecto
COPY . .

# Compilar Spring Boot
RUN mvn clean package -DskipTests


# =========================
# Etapa 2: Ejecutar
# =========================
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copiar el JAR generado
COPY --from=build /app/target/task-0.0.1-SNAPSHOT.jar app.jar

# Puerto utilizado por Render/Spring Boot
EXPOSE 8080

# Ejecutar aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
