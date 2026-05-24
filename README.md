# CulinaryExchange — Final DevOps Task

Веб-приложение с рецептами на **Spring Boot 3**, упакованное в контейнеры с централизованным сбором логов через **Loki/Promtail** и визуализацией в **Grafana**.

---

## 🚀 Быстрый запуск

### Сборка образа
Скрипт принимает тег образа через флаг `-t`:
```bash
./build.sh -t <тег>
# Примеры:
./build.sh -t v1.0
./build.sh -t final-test
```

### Запуск стека
```bash
./run.sh -t <тег>
# Примеры:
./run.sh  -t v1.0
./run.sh -t final-test
```
![build](screenshots/build.png)
![run](screenshots/run.png)



### Доступ к сервисам

| Сервис | URL | Логин/пароль |
|--------|-----|-------------|
| Приложение (логин) | `http://localhost:8080/culinary` | — |
| Grafana | `http://localhost:3000` | `admin` / `admin` |
| Loki API | `http://localhost:3100` | — |


### Остановка

```bash
docker compose down -v›
```

---

## 📸 Скриншоты

### 1. Приложение работает
![App Working](screenshots/app_login.png)
*Страница входа `/culinary/login` — Spring Security загружает форму авторизации*

![App Working](screenshots/app_register.png)
*Страница регистрации `/culinary/register` — Spring Security загружает форму регистрации*

![App Working](screenshots/app_register_fill.png)
*Страница регистрации заполненная

![App Working](screenshots/app_main.png)
*Главная страница `/culinary/` — Spring Security загружает главную страницу*

![App Working](screenshots/app_profile_1.png)
![App Working](screenshots/app_profile_2.png)
*Страница профиля пользователя `/culinary/profile/:id` — Spring Security загружает страницу профиля пользователя*

![App Working](screenshots/app_create_recipe_1.png)
![App Working](screenshots/app_create_recipe_2.png)
*Страница создания рецепта `/culinary/recipe/create` — Spring Security загружает страницу создания рецепта*

![App Working](screenshots/app_cookbook.png)
*Страница книги рецептов текущего пользователя `/culinary/cookbook` — Spring Security загружает книги рецептов текущего пользователя*

![App Working](screenshots/app_search.png)
*Страница поиска рецептов `/culinary/search` — Spring Security загружает страницу поиска*

![App Working](screenshots/app_favorite_recieps.png)
*Страница избранных рецептов текущего пользователя `/culinary/favoriteRecipes` — Spring Security загружает страницу избранных рецептов текущего пользователя*


### 2. Все контейнеры запущены
![Docker Compose PS](screenshots/docker_ps.png)
*`culinary-app`, `postgres`, `loki`, `grafana`, `promtail` — все сервисы в статусе `Up`*

### 3. Логи приложения в stdout
![App Logs](screenshots/app_logs.png)
*Приложение пишет логи в консоль*

### 4. Логи каждого контейнера в Grafana (Loki)

Grafana через Loki отображает логи всех сервисов стека. Ниже показаны логи каждого контейнера отдельно:

#### 4.1 Логи приложения (culinary-app)
![Grafana Logs - App](screenshots/grafana_logs_app_1.png)
![Grafana Logs - App](screenshots/grafana_logs_app_2.png)
*Логи Spring Boot приложения: Hibernate SQL-запросы, Spring Security, контроллеры.  

#### 4.2 Логи PostgreSQL
![Grafana Logs - Postgres](screenshots/grafana_logs_postgres_1.png)
![Grafana Logs - Postgres](screenshots/grafana_logs_postgres_2.png)

*Логи базы данных: подключения, запросы, checkpoint'ы.  

#### 4.3 Логи Loki
![Grafana Logs - Loki](screenshots/grafana_logs_loki_1.png)
![Grafana Logs - Loki](screenshots/grafana_logs_loki_2.png)
*Логи системы хранения логов: инициализация, приём логов от Promtail.  

#### 4.4 Логи Grafana
![Grafana Logs - Grafana](screenshots/grafana_logs_grafana_1.png)
![Grafana Logs - Grafana](screenshots/grafana_logs_grafana_2.png)
*Логи веб-интерфейса: HTTP-запросы, provisioning datasources, алерты.  

#### 4.5 Логи Promtail
![Grafana Logs - Promtail](screenshots/grafana_logs_promtail_1.png)
![Grafana Logs - Promtail](screenshots/grafana_logs_promtail_2.png)
*Логи агента сбора логов: сканирование контейнеров, отправка в Loki.  

---

**Все 5 контейнеров централизованно логируют в Loki** — это доказывает, что система сбора логов работает корректно для всего стека приложения.

---
