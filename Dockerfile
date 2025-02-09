# Stage 1: Сборка приложения с использованием Gradle
FROM gradle:8.11.1-jdk17 AS build
WORKDIR /app
# Копируем все файлы проекта в контейнер. Опция --chown гарантирует, что файлы будут принадлежать пользователю gradle.
COPY --chown=gradle:gradle . .
# Собираем приложение и создаём bootJar без запуска демона Gradle
RUN gradle clean bootJar --no-daemon

# Stage 2: Запуск приложения
FROM openjdk:17-jdk-alpine
WORKDIR /app
# Копируем сгенерированный jar-файл из стадии сборки. Обычно он находится в каталоге build/libs
COPY --from=build /app/build/libs/*.jar app.jar
# Указываем порт, который будет прослушивать приложение
EXPOSE 8080
# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]