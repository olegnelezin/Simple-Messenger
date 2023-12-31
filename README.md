# Тестовое задание "Мессенджер" для компании Релэкс
Необходимо было разработать <b>RESTful API</b> приложение на <b>Java</b> с использованием фреймворка <b>Spring Boot</b>. Данные, получаемые и отправляемые приложением, должны быть в формате <b>JSON</b>.
## Стэк
Java, Spring boot, Spring Security, Jwt, PostgreSQL
## Реализованные требования
<ul>
  <li><b>Регистрация и авторизация:</b></li>
    <ul>
      <li>есть API для регистрации, сохраняющий пользователя в хранилище;</li>
      <li>есть хэширование паролей;</li>
      <li>есть API, позволяющий залогиниться в системе и сохраняющее информацию о сессии любым способом;</li>
      <li>есть поддержка Spring Security;
      <li>информация о сессии хранится в JWT токенах и передается в HTTP хэдерах или куках;</li>
      <li>есть API, позволяющий завершить текущую сессию и разлогиниться;</li>
      <li>есть механизмы защиты от обхода разлогина (например, при использовании JWT токенов можно инвалидировать их).</li>
    </ul>
  <li><b>Профиль пользователя:</b></li>
    <ul>
      <li>есть API, позволяющий изменять базовую информацию профиля;</li>
      <li>есть API, позволяющий обновить пароль;</li>
      <li>есть API, позволяющий удалить аккаунт пользователя;</li>
      <li>реализован перевод профиля в статус “Не активен” с дальнейшей возможностью восстановить профиль в течение некоторого времени.</li>
    </ul>
  <li><b>Социальная часть:</b></li>                 
    <ul>
      <li>есть API, позволяющий отправить другому пользователю сообщение, реализована проверка на существование пользователя;</li>
      <li>есть API, позволяющий просматривать историю сообщений.</li>
    </ul>
  <li><b>Дополнительно:</b></li>
    <ul>
      <li>Использована база данных PostgreSQL.</li>
    </ul>
</ul>

## База данных
В качестве базы данных была использована PostgreSQL.
### Таблица "users"
| Колонка   | Тип     | Комментарий                                      |
|-----------|---------|--------------------------------------------------|
| id        | BIGINT  | Айди пользователя, первичный ключ, автоинкремент |
| username  | VARCHAR | Никнейм(логин), уникальный, не нулевой           |
| password  | VARCHAR | Пароль, не нулевой                               |
| firstname | VARCHAR | Имя, не нулевой                                  |
| lastname  | VARCHAR | Фамилия, не нулевой                              |
| email     | VARCHAR | Электронная почта, уникальный, не нулевой        |
| is_active | BOOLEAN | Активен/неактивен, не нулевой                    |

### Таблица "user_roles"
| Колонка   | Тип     | Комментарий                                 |
|-----------|---------|---------------------------------------------|
| user_id   | BIGINT  | Айди пользователя, внешний ключ на users.id |
| role_id   | INTEGER | Айди роли, внешний ключ на roles.id         |

### Таблица "roles"
| Колонка   | Тип     | Комментарий                              |
|-----------|---------|------------------------------------------|
| id        | INTEGER | Айди роли, первичный ключ, автоинкремент |
| name      | VARCHAR | Название роли, уникальный, не нулевой    |

### Таблица "messages"
| Колонка              | Тип     | Комментарий                                   |
|----------------------|---------|-----------------------------------------------|
| id                   | BIGINT  | Айди сообщения, первичный ключ, автоинкремент |
| user_id_who_send     | BIGINT  | Айди отправителя, уникальный, не нулевой      |
| user_id_who_recieve  | BIGINT  | Айди получателя, не нулевой                   |
| username_who_send    | VARCHAR | Имя получателя, не нулевой                    |
| username_who_recieve | VARCHAR | Имя получателя, не нулевой                    |
| message              | VARCHAR | Сообщение                                     |

## Запросы
Для <b>каждого запроса</b>, за исключением "<i>/login</i>", "<i>/register</i>" и "<i>/activate</i>", требуется <b>валидный jwt токен</b>, который должен передаваться в HTTP хэдере типа Bearer.<br>
<b>Jwt токен</b> генерируется после успешного входа пользователя с помощью "<i>/login</i>".
#### POST /register
Регистрация пользователя. Пример запроса:
```
{
  "username": "relex",
  "password": "root",
  "firstname": "Oleg",
  "lastname": "Nelezin",
  "email": "astravsu@gmail.com" 
}
```
Поля "username" и "email" должны быть <b>уникальными</b>, иначе выводится ошибка.
#### POST /login
Авторизация пользователя. Пример запроса:
```
{
  "username": "relex",
  "password": "root"
}
```
Пример ответа:
```
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWxleCIsImlhdCI6MTY5Njk0OTM1MiwiZXhwIjoxNjk2OTQ5OTUyfQ.g7zW6XOmz8T3opJl-RxEBEXXULZWNlsrDHCRAKGg574",
  "type": "Bearer",
  "id": 1,
  "username": "relex",
  "firstname": "Oleg",
  "lastname": "Nelezin",
  "email": "astravsu@gmail.com",
  "roles": [
      "ROLE_USER"
  ]
}
```
Далее следует вставить токен в HTTP хэдер.
#### GET /info
Вывод информации о пользователе. Пример ответа:
```
{
    "username": "relex",
    "firstname": "Oleg",
    "lastname": "Nelezin",
    "email": "astravsu@gmail.com"
}
```
#### GET /signout 
Выход из текущей сессии.
Разлогинивание реализовано путем занесения токена в чёрный список.
Для реализации черного списка была использована библиотека https://github.com/jhalterman/expiringmap
#### POST /change-information
Изменение информации профиля. Примеры запросов:
```
{
  "firstname": "Jordan",
}
```
```
{
  "firstname": "Jordan",
  "lastname": "Belfort,
  "email": "relex@company.ru"
}
```
#### POST /change-password
Изменение пароля пользователя. Пример запроса:
```
{
  "old_password": "root",
  "new_password": "relexthebest"
}
```
#### GET /delete
Удаление аккаунта пользователя.
#### GET /deactivate
Деактивация аккаунта пользователя, т.е. перевод профиля в статус "Не активен", с возможностью восстановить аккаунт в будущем.
#### POST /activate
Активация аккаунта пользователя, если он был деактивирован ранее. Для активации аккаунта требуется ввести логин и пароль. Пример запроса:
```
{
  "username": "relex",
  "password": "relexthebest"
}
```
#### POST /send-message
Отправка сообщения пользователю. Если пользователя не существует - выводится ошибка. Пример запроса
```
{
  "username": "relex",
  "message": "Hello, relex!"
}
```
#### GET /get-messages
Вывод всех сообщений пользователя. Пример ответа:
```
{
    "messages": [
        {
            "id": 8,
            "usernameWhoSend": "oleg",
            "usernameWhoRecieve": "relex",
            "message": "Hello, relex!"
        },
        {
            "id": 9,
            "usernameWhoSend": "relex",
            "usernameWhoRecieve": "oleg",
            "message": "Hello, oleg!"
        }
    ]
}
```

