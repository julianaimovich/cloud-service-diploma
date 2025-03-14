
## ☁️ «Облачное хранилище»

REST-сервис, позволяющий хранить файлы и работать с ними.

## 📖 Оглавление

1. [Описание](#-описание)
2. [Стек](#-стек)
3. [Запуск приложения](#-запуск-приложения)
4. [Конфигурирование](#️-конфигурирование)
5. [Хранение учетных данных пользователей](#-хранение-учетных-данных-пользователей)
6. [Swagger UI](#-swagger-ui)
7. [Тестирование](#-тестирование)
8. [Автор](#-автор)

---

## ℹ️ Описание

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

## ⚙️ Конфигурирование

Настройки приложения находятся в файлах:


```application.yml``` – основной конфигурационный файл, который содержит в себе основные настройки. Также есть два файла настроек, которые применяются в зависимости от того, что необходимо сделать. 
Созданы два профиля – ```prod``` и ```test```. Дефолтным профилем является ```prod```, профиль ```test``` включается с помощью аннотации ```@ActiveProfiles({"test"})``` в классах с unit-тестами.  
Пример конфигурации ```application.yml```:

```
spring:
  application:
    name: cloud-service           # Название приложения
  profiles:
    active: prod                  # Дефолтный активный профиль
  jackson:
    serialization:
      fail-on-empty-beans: false  # Для предотвращения ошибки при сериализации Jackson
  servlet:
    multipart:
      enabled: true
      max-file-size: 10MB
      max-request-size: 15MB

management:                       # Настройки Spring Boot Actuator для контроля статуса сервера 
  endpoints:
    web:
      exposure:
        include: health
  endpoint:
    health:
      show-details: always
      probes.enabled: true
  health:
    db:
      enabled: true

logging:
  level:
    org.springframework: WARN
    org.apache.catalina: WARN
    org.hibernate: ERROR
    org.springframework.security: INFO
    io.restassured: INFO
  config: classpath:log4j2.xml
```

```application-prod.yml``` – конфигурационный файл, содержащий настройки профиля ```prod```, необходимого для работы сервера с БД MySQL. Пример конфигурации ```application-prod.yml```:

```
spring:  
  jpa:  
    database-platform: org.hibernate.dialect.MySQL8Dialect  
    hibernate:  
      ddl-auto: create  
    defer-datasource-initialization: true  
  datasource:  
    url: jdbc:mysql://host.docker.internal:3306/netology?useSSL=false&serverTimezone=UTC  
    driver-class-name: com.mysql.cj.jdbc.Driver  
    username: root  
    password: root  
  sql:  
    init:  
      mode: always
```

```application-test.yml``` – конфигурационный файл, содержащий настройки профиля ```test```, необходимого для прохождения тестов с Mockito. Содержит настройки для in-memory H2 базы данных, которая позволяет мокать обращения к БД в репозиториях.   
Пример конфигурации ```application-test.yml```:

```
spring:  
  sql:  
    init:  
      mode: never  
  jpa:  
    database-platform: org.hibernate.dialect.H2Dialect  
    properties:  
      hibernate.hbm2ddl.import_files: import.sql  
    hibernate:  
      ddl-auto: create  
  datasource:  
    driver-class-name: org.h2.Driver  
    url: jdbc:h2:mem:netology;DB_CLOSE_DELAY=-1  
    username: sa  
    password:  
```

```log4j2.xml``` – конфигурационный файл, содержащий настройки логирования.   
Пример конфигурации ```log4j2.xml```:

```
<?xml version="1.0" encoding="UTF-8"?>
<Configuration shutdownHook="disable">
    <Properties>
        <property name="CONSOLE_LOG_PATTERN" value="%style{%d{ISO8601}} %highlight{%-5level}[%style{%tid:%t}{bright,blue}] %style{%C{1.}}{bright,yellow}: %msg%n%throwable" />
        <property name="FILE_LOG_PATTERN" value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%tid:%tn] %-5p %c - %m%n" />
        <property name="LOG_PATH" value="./logs"/>
    </Properties>

    <Appenders>
        <Console name="toConsole">
            <PatternLayout pattern="${CONSOLE_LOG_PATTERN}" />
        </Console>

        <RollingFile name="toTrace"
                     fileName="${LOG_PATH}/trace.log"
                     filePattern="${LOG_PATH}/history/%d{yyyy-MM-dd}/trace-%d{yyyy-MM-dd_HH-mm}.log">
            <PatternLayout pattern="${FILE_LOG_PATTERN}" />
            <Policies>
                <!-- rollover on startup, daily and when the file reaches 20 MegaBytes -->
                <OnStartupTriggeringPolicy />
                <SizeBasedTriggeringPolicy size="20 MB" />
                <TimeBasedTriggeringPolicy />
            </Policies>
        </RollingFile>

        <RollingFile name="toTestTrace"
                     fileName="${LOG_PATH}/test_trace.log"
                     filePattern="${LOG_PATH}/history/%d{yyyy-MM-dd}/test_trace-%d{yyyy-MM-dd_HH-mm}.log">
            <PatternLayout pattern="${FILE_LOG_PATTERN}" />
            <Policies>
                <!-- rollover on startup, daily and when the file reaches 20 MegaBytes -->
                <OnStartupTriggeringPolicy />
                <SizeBasedTriggeringPolicy size="20 MB" />
                <TimeBasedTriggeringPolicy />
            </Policies>
        </RollingFile>
    </Appenders>

    <Loggers>
        <!-- Логирование HTTP-запросов и ответов -->
        <logger name="ru.netology" level="INFO" additivity="false">
            <AppenderRef ref="toTestTrace" />
            <AppenderRef ref="toConsole" />
        </logger>

        <logger name="org.springframework.web.filter" level="INFO" additivity="false">
            <AppenderRef ref="toConsole"/>
            <AppenderRef ref="toTestTrace"/>
        </logger>

        <logger name="org.apache.catalina.filters.RequestDumperFilter" level="ERROR" additivity="false">
            <AppenderRef ref="toConsole"/>
            <AppenderRef ref="toTestTrace"/>
        </logger>

        <logger name="org.springframework.web.servlet.DispatcherServlet" level="ERROR" additivity="false">
            <AppenderRef ref="toConsole"/>
            <AppenderRef ref="toTestTrace"/>
        </logger>

        <logger name="org.reflections.Reflections" level="ERROR" additivity="false">
            <AppenderRef ref="toConsole" />
            <AppenderRef ref="toTrace" />
        </logger>

        <logger name="io.restassured.filter" level="INFO" additivity="false">
            <AppenderRef ref="toConsole"/>
            <AppenderRef ref="toTestTrace"/>
        </logger>

        <Root level="INFO">
            <AppenderRef ref="toConsole" />
            <AppenderRef ref="toTestTrace" />
            <AppenderRef ref="toTrace" />
        </Root>
    </Loggers>
</Configuration>
```
---

## 💾 Хранение учетных данных пользователей

При старте приложения разворачиваются таблицы ```users``` и ```authorities``` для MySQL  и для H2.  
Скрипты заполнения таблиц данными находятся в ```src/main/resources```:  
- ```data.sql``` – содержит в себе данные, которыми заполняются таблицы в БД MySQL
- ```import.sql``` – содержит в себе данные, которыми заполняются таблицы в БД H2

---

## 🌐 Swagger UI

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
