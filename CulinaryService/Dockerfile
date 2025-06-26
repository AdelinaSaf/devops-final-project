# Этап сборки
FROM maven:3.9.10-eclipse-temurin-21 AS builder

# Устанавливаем рабочую директорию
WORKDIR /workspace/app

# Копируем только POM файл сначала (для кэширования зависимостей)
COPY pom.xml .


# Копируем исходный код
COPY src src

# Собираем проект
RUN mvn clean package -DskipTests

# Финальный образ
FROM openjdk:21-slim
VOLUME /tmp
COPY --from=builder /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]