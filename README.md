# Silent Signals

SOS siqnalı, təcili kontaktlar, JWT autentifikasiya, Redis sessiyası və real vaxt WebSocket yayımı olan **Spring Boot** backend layihəsi.

---

## Texnologiyalar

| Sahə | Seçim |
|------|--------|
| Java | 17 |
| Framework | Spring Boot 3.4 |
| DB | PostgreSQL |
| Cache / SOS sessiyası | Redis |
| API sənədləşməsi | SpringDoc OpenAPI (Swagger UI) |
| Təhlükəsizlik | Spring Security, JWT (access + refresh), BCrypt |

---

## Tələblər

- **JDK 17+**
- **Maven** (və ya layihə ilə gələn `./mvnw` / `mvnw.cmd`)
- **PostgreSQL 16+** (lokal və ya Docker)
- **Redis** (SOS TTL və bildiriş axını üçün; söndürmək üçün bax: `APP_REDIS_ENABLED`)

---

## Sürətli başlanğıc (lokal)

1. PostgreSQL-də boş verilənlər bazası yaradın (məsələn `silent_signals_db`).
2. Redis-i işə salın (standart `localhost:6379`).
3. Mühit dəyişənlərini təyin edin (minimum):

```bash
set DB_USERNAME=postgres
set DB_PASSWORD=postgres
set JWT_SECRET=<uzun-tesadufi-access-secret>
set JWT_REFRESH_SECRET=<uzun-tesadufi-refresh-secret>
```

4. Tətbiqi işə salın:

```bash
./mvnw spring-boot:run
```

Windows:

```text
mvnw.cmd spring-boot:run
```

Tətbiq: **http://localhost:8011**

---

## Docker ilə

Layihəni jar ilə yığmaq, sonra konteynerdə işə salmaq:

```bash
./mvnw -DskipTests package
docker compose up --build
```

Əvvəlcədən `GMAIL_PASS`, `JWT_SECRET`, `JWT_REFRESH_SECRET` təyin etmək məsləhətdir (compose faylında istinad var).

---

## Əsas mühit dəyişənləri

| Dəyişən | Təsvir |
|---------|--------|
| `SPRING_DATASOURCE_URL` | JDBC URL (məs. `jdbc:postgresql://localhost:5432/silent_signals_db`) |
| `DB_USERNAME` / `DB_PASSWORD` | DB istifadəçi və şifrə |
| `JWT_SECRET` | Access token imza açarı (uzun, təsadüfi) |
| `JWT_REFRESH_SECRET` | Refresh token imza açarı |
| `SPRING_REDIS_HOST` / `SPRING_REDIS_PORT` | Redis ünvanı |
| `APP_REDIS_ENABLED` | `false` — Redis avtokonfiqurasiyasını söndürür (məs. test/inkişaf) |
| `MAIL_USERNAME` | SMTP istifadəçi (məs. Gmail) |
| `GMAIL_PASS` | SMTP şifrə / app password |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` (dev), prod üçün `validate` + `spring.profiles.active=prod` |
| `SPRING_PROFILES_ACTIVE` | `prod` — `application-prod.properties` (ddl `validate`) |

---

## API və sənədlər

- **Swagger UI**: `http://localhost:8011/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8011/v3/api-docs`

### Qısa endpoint xülasəsi

| Metod | Yol | Qeyd |
|--------|-----|------|
| POST | `/api/auth/register` və ya `/api/auth/reg-user` | Qeydiyyat (201 / 409) |
| POST | `/api/auth/login` | Giriş (JWT + refresh) |
| POST | `/api/auth/refresh-token` | Yeni access token |
| POST | `/api/sos/send` | SOS göndərmə (JWT, `ROLE_USER`) |
| GET | `/api/sos/history` | Öz SOS tarixçəsi |
| POST | `/api/sos/resolve` | SOS həlli (`alertId` ilə) |
| POST | `/api/contacts` | Təcili kontakt əlavə |
| GET | `/api/contacts/me` | Öz kontaktları |
| DELETE | `/api/contacts/{id}` | Kontakt silmə |

Protected endpoint-lər üçün header: `Authorization: Bearer <access_token>`.

---

## WebSocket (real vaxt)

- **Endpoint**: SockJS/STOMP — `/ws`
- **Qoşulma**: `token` query parametri ilə JWT göndərin (məs. `.../ws?token=<access_jwt>`).
- **Topic**: `/topic/sos` — SOS və həll hadisələri JSON mesaj kimi yayılır.

---

## Əməliyyat (Actuator)

Açıq endpoint-lər (konfiqurasiyaya uyğun): məs. `GET /actuator/health`, `/actuator/info`.

---

## Testlər

```bash
./mvnw test
```

Test mühiti H2 və Redis söndürülmüş konfiqurasiya ilə işləyir (`src/test/resources/application.properties`).

---

## Layihə strukturu (qısa)

```text
src/main/java/.../config/     — Security, OpenAPI, Redis, WebSocket
src/main/java/.../controller/ — REST API
src/main/java/.../service/    — Biznes məntiqi
src/main/java/.../repository/ — Spring Data JPA
src/main/java/.../dto/        — Sorğu/cavab modelləri
src/main/resources/           — application.properties (+ prod profil)
```

---

## Təhlükəsizlik xatırlatmaları

- Prod-da **`JWT_SECRET`** və **`JWT_REFRESH_SECRET`** mütləq təsadüfi və uzun olsun; repoda heç vaxt hardkod etməyin.
- Gmail üçün adətən **App Password** istifadə olunur.
- Docker/production üçün **`SPRING_PROFILES_ACTIVE=prod`** və sxemanın idarə olunması (məs. migrasiya) tövsiyə olunur.

---

