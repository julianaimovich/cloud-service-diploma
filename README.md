
## 📌 «Облачное хранилище»

REST-сервис, позволяющий хранить файлы и работать с ними.

## 📖 Оглавление

1. [Описание](-#описание)
2. [Стек](-#стек)
3. [Запуск приложения](-#запуск-приложения)
4. [Конфигурация](-#конфигурация)
5. [Использование](-#использование)
6. [API Эндпоинты](-#api-эндпоинты)
7. [Тестирование](-#тестирование)
8. [Разработка](-#разработка)
9. [Авторы](-#авторы)

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

## 📌 Использование

Приведите примеры работы приложения. Например, если это REST API, покажите примеры запросов:

### Создание задачи
curl -X POST http://localhost:8080/api/tasks \
     -H "Content-Type: application/json" \
     -d '{"title": "Новая задача", "description": "Описание задачи"}'

### Получение списка задач
curl -X GET http://localhost:8080/api/tasks

---

## 📌 API Эндпоинты

| Метод | URL | Описание |
|--------|----------------|-------------------------------|
| GET | /api/tasks | Получить все задачи |
| GET | /api/tasks/{id} | Получить задачу по ID |
| POST | /api/tasks | Создать новую задачу |
| PUT | /api/tasks/{id} | Обновить задачу |
| DELETE | /api/tasks/{id} | Удалить задачу |

---

## 🧪 Тестирование

Как запустить тесты:

./mvnw test
или
./gradlew test

---

## 🛠 Разработка

Если другие разработчики захотят внести свой вклад, укажите инструкцию:

1. Форкните репозиторий
2. Создайте новую ветку: git checkout -b feature-name
3. Внесите изменения и закоммитьте: git commit -m "Добавил новую фичу"
4. Запушьте изменения: git push origin feature-name
5. Создайте Pull Request

---

## 👥 Авторы

- Имя Фамилия – [GitHub](https://github.com/username)
