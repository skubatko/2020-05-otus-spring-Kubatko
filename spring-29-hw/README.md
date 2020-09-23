# Домашнее задание к уроку 29: Монолиты vs. Microservices (Round 2), Spring Boot Actuator - must have в микросервисах

## Использовать метрики, healthchecks и logfile

Цель: реализовать production-grade мониторинг и прозрачность в приложении

Результат: приложение с применением Spring Boot Actuator

Данное задание выполняется на основе одного из реализованных Web-приложений

1. Подключить Spring Boot Actuator в приложение.
2. Включить метрики, healthchecks и logfile.
3. Реализовать свой собственный HealthCheck индикатор
4. UI для данных от Spring Boot Actuator реализовывать не нужно.
5. Опционально: переписать приложение на HATEOAS принципах с помощью Spring Data REST Repository

## Links
http://localhost:8080/ - основное приложение
http://localhost:8080/api - swagger
http://localhost:8080/rest - HAL browser
http://localhost:8080/rest/books - HATEOAS
http://localhost:8080/actuator/info - информация по сборке
http://localhost:8080/actuator/health - статус работы системы
