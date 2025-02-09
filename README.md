
## 📌 «Облачное хранилище»

REST-сервис, позволяющий хранить файлы и работать с ними.

## 📖 Оглавление

1. [Описание](#-описание)
2. [Стек](#-стек)
3. [Запуск приложения](#-запуск-приложения)
4. [Конфигурация](#️-конфигурация)
5. [Хранение учетных данных пользователей](#-хранение-учетных-данных-пользователей)
6. [Swagger UI](#-swagger-ui)
7. [Тестирование](#-тестирование)
8. [Автор](#-автор)

---

## 📌 Описание

Этот backend-сервис представляет собой REST API для работы с файлами. Позволяет загружать, скачивать, редактировать и удалять файлы.

---

## 🛠 Стек

- Java 17
- Spring Boot 3
- MySQL
- Hibernate
- Docker
- JUnit 5
- Mockito
- Testcontainers

---

## 🚀 Запуск приложения

1. Клонируйте репозиторий:
  ```
  git clone https://github.com/julianaimovich/cloud-service-diploma.git
  ```
   
2. Приложение запускается в docker контейнере с помощью команды ```docker-compose up --build``` в корневой директории проекта.

---

## ⚙️ Конфигурация

Настройки приложения находятся в файлах:

```application.yml``` – основной конфигурационный файл, который содержит в себе основные настройки и профили Spring, которые могут быть включены в зависимости от необходимого функционала. По умолчанию выбран профиль ```prod```, профиль ```test``` включается с помощью аннотации ```@ActiveProfiles({"test"})``` в классах с unit-тестами. Пример конфигурации ```application.yml```:

```
spring.application.name: cloud-service # Название приложения

spring.profiles.include: prod, test # Профили Spring
spring.profiles.active: prod # Активный профиль по умолчанию  
spring.jackson.serialization.fail-on-empty-beans: false # Для предотвращения ошибки при сериализации некоторых объектов библиотекой Jackson
```

```application-prod.yml``` – конфигурационный файл, содержащий настройки профиля ```prod```, необходимого для работы сервера с БД MySQL. Пример конфигурации ```application-prod.yml```:

```
spring.config.activate.on-profile: prod # Название профиля 
  
spring.jpa.database-platform: org.hibernate.dialect.MySQLDialect  
spring.datasource.url: jdbc:mysql://localhost:3306/netology  
spring.datasource.driver-class-name: com.mysql.cj.jdbc.Driver  
spring.datasource.username: root  
spring.datasource.password: root  
spring.jpa.hibernate.ddl-auto: update
```

```application-test.yml``` – конфигурационный файл, содержащий настройки профиля ```test```, необходимого для прохождения тестов с Mockito. Содержит настройки для in-memory H2 базы данных, которая позволяет мокать обращения к БД в репозиториях. Пример конфигурации ```application-test.yml```:

```
spring.config.activate.on-profile: test # Название профиля
  
spring.jpa.database-platform: org.hibernate.dialect.H2Dialect  
spring.datasource.driver-class-name: org.h2.Driver  
spring.datasource.url: jdbc:h2:mem:db;DB_CLOSE_DELAY=-1  
spring.datasource.username: sa  
spring.datasource.password: sa  
spring.jpa.defer-datasource-initialization: true  
spring.h2.console.enabled: true  
spring.h2.console.path: /h2-console  
spring.h2.console.settings.trace: false  
spring.h2.console.settings.web-allow-others: false
```
---

## 📌 Хранение учетных данных пользователей

При старте приложения разворачиваются таблицы ```users``` и ```authorities``` для MySQL  и для H2.  
Скрипты заполнения таблиц данными находятся в ```src/main/resources```:  
- ```data.sql``` – содержит в себе данные, которыми заполняются таблицы в БД MySQL
- ```import.sql``` – содержит в себе данные, которыми заполняются таблицы в БД H2

---

## 📌 Swagger UI

Спецификацию с подробным описанием всех методов можно посмотреть в [Swagger UI](https://julianaimovich.github.io/cloud-service-diploma/) проекта.

---

## 🧪 Тестирование

Тесты можно запустить командой ```./gradlew test``` в корневой папке проекта.

Реализованы как Unit-тесты, так и интеграционные: 
- Unit-тесты для репозиториев c использованием ```Spring Data JPA```
- Unit-тесты для служб с использованием библиотеки ```Mockito``` 
- Unit-тесты для контроллеров с использованием ```Spring MVC```
- Интеграционные тесты с использованием ```Testcontainers```

---

## 👥 Автор

- Юлия Наймович – [GitHub](https://github.com/julianaimovich?tab=repositories)
