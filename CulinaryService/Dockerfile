# Этап 1: Сборка
FROM maven:3.9.10-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Этап 2: Запуск
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Копируем WAR файл (он теперь исполняемый благодаря spring-boot-maven-plugin)
COPY --from=builder /app/target/culinaryExchange-1.0-SNAPSHOT.war app.war

ENTRYPOINT ["java", "-jar", "app.war"]