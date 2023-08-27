# Дипломный проект по профессии «Тестировщик»

# [Веб-сервис заказа путешествия](https://github.com/netology-code/qa-diploma)

## Описание приложения

### Бизнес часть

Приложение — это веб-сервис, который предлагает купить тур по определённой цене двумя способами:
1. Обычная оплата по дебетовой карте.
2. Выдача кредита по данным банковской карты.

Само приложение не обрабатывает данные по картам, а пересылает их банковским сервисам:
* сервису платежей, далее - Payment Gate;
* кредитному сервису, далее - Credit Gate.

Приложение в собственной СУБД должно сохранять информацию о том, успешно ли был совершён платёж и каким способом. Данные карт при этом сохранять не допускается.

### Техническая часть

Само приложение расположено в файле [`aqa-shop.jar`](https://github.com/AlexPanasko/QA-Diploma/blob/main/artifacts/aqa-shop.jar) и запускается на порту `8080`.

В файле [`application.properties`](https://github.com/AlexPanasko/QA-Diploma/blob/main/application.properties) приведён ряд типовых настроек:
* учётные данные и url для подключения к СУБД
* url-адреса банковских сервисов

### СУБД

Заявлена поддержка двух СУБД:
* MySQL
* PostgreSQL

Учётные данные и url для подключения задаются в файле [`application.properties`](https://github.com/AlexPanasko/QA-Diploma/blob/main/application.properties).

### Банковские сервисы

Разработчики подготовили симулятор банковских сервисов, который может принимать запросы в нужном формате и генерировать ответы.

Симулятор расположен в каталоге [`gate-simulator`](https://github.com/AlexPanasko/QA-Diploma/tree/main/gate-simulator). Для запуска необходимо перейти в этот каталог.

Запускается симулятор командой `npm start` на порту 9999.

Симулятор позволяет для заданного набора карт генерировать предопределённые ответы.

Набор карт представлен в формате JSON в файле [`data.json`](https://github.com/AlexPanasko/QA-Diploma/blob/main/gate-simulator/data.json).

Сервис симулирует и Payment Gate и Credit Gate.
___
## Начало работы

### Предусловия

На ПК необходимо установить:
`IntelliJ IDEA`, `Google Chrome`, `Git`, `Docker Desktop`.

### Установка и запуск

1. Запустить `Docker Desktop`.
2. Склонировать код репозитория проекта и открыть в `IntelliJ IDEA`.
3. Выполнить команду в терминале для запуска контейнеров из файла `docker-compose.yml`:
```
docker-compose up
```

4. Проверить статус контейнеров командой:

```
docker-compose ps
```
Убеждаемся, что статус контейнеров `UP`.

5. После развертывания контейнера для запуска `SUT` в зависимости от выбранной для работы `СУБД` выполнить команду в консоли:

- Для PostgreSQL:

```
java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar ./artifacts/aqa-shop.jar
```
- Для MySQL:

```
java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar ./artifacts/aqa-shop.jar
```

Сервис будет доступен по адресу: _http://localhost:8080/_

6. Запустить тесты командой в терминале:

- Для PostgreSQL:

```
./gradlew clean test "-Ddatasource.url=jdbc:postgresql://localhost:5432/app"
```

- Для MySQL:

```
./gradlew clean test "-Ddatasource.url=jdbc:mysql://localhost:3306/app"
```
___

### Дополнительные сведения:

Для просмотра отчета `Allure Report` после выполнения тестов ввести в терминале:
```
./gradlew allureServe
```
___
#### После окончания работы:
1. Завершить работу `SUT` сочетанием клавиш `CTRL + C` с подтверждением действия в терминале вводом `Y`.
2. Завершить работу контейнеров командой в консоли:
```
docker-compose down
```
___