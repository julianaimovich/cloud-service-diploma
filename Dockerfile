# Stage 1: Сборка приложения с использованием Gradle
FROM gradle:8.11.1-jdk17 AS build
WORKDIR /app

# Определяем временный каталог для Gradle-кэша (чтобы избежать блокировки файлов)
ENV GRADLE_USER_HOME /tmp/gradle-cache

# Копируем только нужные файлы для сборки, исключая .gradle и ненужные артефакты
COPY --chown=gradle:gradle build.gradle settings.gradle gradlew /app/
COPY --chown=gradle:gradle src /app/src
COPY --chown=gradle:gradle gradle /app/gradle

# Собираем приложение и создаём bootJar без запуска демона Gradle
RUN gradle clean bootJar --no-daemon --gradle-user-home $GRADLE_USER_HOME

# Stage 2: Запуск приложения
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Копируем сгенерированный jar-файл из стадии сборки
COPY --from=build /app/build/libs/*.jar app.jar

# Указываем порт, который будет прослушивать приложение
EXPOSE 8080

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]