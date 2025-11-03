# Dockerfile multi-stage básico para projeto Spring Boot com Maven    
# Stage 1: build com Maven (usa JDK 17)
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copia apenas o pom primeiro para aproveitar cache do Docker
COPY pom.xml .
# Copia o código fonte
COPY src ./src

# Compila o projeto e empacota sem rodar os testes para build rápido
RUN mvn -B -DskipTests package

# Stage 2: runtime usando JRE 17 leve
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia o jar gerado do stage de build
COPY --from=build /app/target/*.jar /app/app.jar

# Porta padrão do Spring Boot
EXPOSE 8080

# Executa o jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]