# VkChatbotApplication

## Описание

Этот проект представляет собой Spring Boot приложение для чат-бота VK.

## Требования

- JDK 21

## Сборка и запуск

1. С помощью ngrok настроить внешний https адрес для локальной
   машины ([dashboard.ngrok.com](https://dashboard.ngrok.com/get-started/setup/windows))

2. Создать сообщество и настроить ключ
   доступа ([dev.vk.com/ru/api/bots](https://dev.vk.com/ru/api/bots/getting-started?ref=old_portal))

3. Настроить Callback API в
   сообществе ([dev.vk.com/ru/api/callback](https://dev.vk.com/ru/api/callback/getting-started))

4. Создайте в папке `src/main/resources` файл конфигурации `application-private.properties`
   и запишите туда используемую версию API, ключ доступа и строку, которую должен вернуть сервер при подтверждении
   адреса
    ```
    vk.version=YOUR_VERSION
    vk.token=YOUR_ACCESS_TOKEN
    vk.confirmation=YOUR_CONFIRMATION_STRING
    ```

5. Запустите приложение:
    ```bash
    ./gradlew buildAndRun
    ```

## Пример использования
https://github.com/KonovkinaEA/vk-chatbot/assets/70512444/32bd2c35-292d-4bac-8175-26df522ea7a3
