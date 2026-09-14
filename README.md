### Truckmanager

Projekt powstał jako połączenie pasji do Programowania i jednej z moich ulubionych gier na Steam, Euro Truck Simulator 2.

W ramach projektu będę uczył się nowych rzeczy, poszukiwanych na rynku pracy, lub takich, które zahaczają o bieżące wyzwania w obecnym miejscu pracy.

---

## 🚀 Uruchomienie aplikacji

### 1. Wymagania wstępne
- **Docker** oraz **Docker Compose**
- **Java 25**
- **Maven** (lub wrapper Mavena)

---

### 2. Uruchomienie infrastruktury i aplikacji (Docker)
W katalogu głównym projektu możesz uruchomić całe środowisko (bazy danych, Keycloak oraz obie mikrousługi) za pomocą Docker Compose:

```bash
docker compose up -d --build
```

W ramach `docker-compose.yaml` uruchamiane są następujące usługi:
- **trucks** (mikrousługa zarządzania pojazdami): `http://localhost:8080`
- **drivers** (mikrousługa zarządzania kierowcami): `http://localhost:8081`
- **Keycloak** (OAuth2 / OIDC): `http://localhost:8180` (realm `truckmanager` jest automatycznie importowany przy starcie)
- **PostgreSQL**: `localhost:5432` (bazy `trucksdb` oraz `keycloakdb`, użytkownik: `root`, hasło: `example`)
- **MongoDB**: `localhost:27017` (użytkownik: `root`, hasło: `example`)
- **Mongo Express** (panel WWW do MongoDB): `http://localhost:8082` (login: `test`, hasło: `test`)

---

### 3. Uruchomienie serwisów lokalnie (Spring Boot)

Alternatywnie możesz uruchomić same bazy danych i Keycloak w Dockerze, a serwisy odpalić lokalnie:

#### Serwis `trucks` (port `8080`)
Zarządza danymi pojazdów (MongoDB).
```bash
# W katalogu trucks/
mvn spring-boot:run
```
*Domyślne zmienne środowiskowe:*
- `MONGO_DB_URL=mongodb://root:example@localhost:27017/trucks?authSource=admin`
- `KEYCLOAK_ISSUER_URI=http://localhost:8180/realms/truckmanager`
- `KEYCLOAK_JWK_SET_URI=http://localhost:8180/realms/truckmanager/protocol/openid-connect/certs`

#### Serwis `drivers` (port `8081`)
Zarządza danymi kierowców (PostgreSQL + Flyway).
```bash
# W katalogu drivers/
mvn spring-boot:run
```
*Domyślne zmienne środowiskowe:*
- `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/trucksdb`
- `SPRING_DATASOURCE_USERNAME=root`
- `SPRING_DATASOURCE_PASSWORD=example`
- `KEYCLOAK_ISSUER_URI=http://localhost:8180/realms/truckmanager`
- `KEYCLOAK_JWK_SET_URI=http://localhost:8180/realms/truckmanager/protocol/openid-connect/certs`

---

## 🔐 Logowanie i Uwierzytelnianie (Keycloak)

Aplikacja korzysta z serwera tożsamości **Keycloak** z realmem `truckmanager` oraz zabezpieczeniami OAuth2 Resource Server (JWT).

### 1. Domyślni użytkownicy testowi

W zaimportowanym realmie `truckmanager` skonfigurowani są następujący użytkownicy:

| Login | Hasło | Przypisana rola | Opis |
|---|---|---|---|
| `admin` | `admin` | `ADMIN` | Pełny dostęp do wszystkich zasobów |
| `wlasciciel` | `wlasciciel` | `WLASCICIEL_FIRMY` | Zarządzanie flotą i kierowcami |
| `kierowca` | `kierowca` | `KIEROWCA` | Dostęp do odczytu danych pojazdów i danych własnych |

> **Konsola administracyjna Keycloak:** `http://localhost:8180`  
> Logowanie do konsoli administratora Keycloak: `admin` / `admin`

---

### 2. Pobieranie tokenu dostępu (JWT)

Aby uzyskać token dostępu (Bearer Token) dla wybranego użytkownika, wykonaj zapytanie POST do endpointu tokenów Keycloaka:

```bash
curl -X POST "http://localhost:8180/realms/truckmanager/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=truckmanager-cli" \
  -d "username=admin" \
  -d "password=admin"
```

W odpowiedzi otrzymasz obiekt JSON zawierający pole `access_token`:
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIs...",
  "expires_in": 1800,
  "token_type": "Bearer",
  ...
}
```

---

### 3. Komunikacja z API aplikacji

Obie mikrousługi zabezpieczone są protokołem OAuth2 / JWT. W każdym zapytaniu do chronionych endpointów należy przekazać pobrany token JWT w nagłówku `Authorization: Bearer <TOKEN>`.

---

#### 🚛 Serwis `trucks` (Port `8080`)

Zarządza danymi pojazdów flotowych.

##### 1. Interfejs Swagger UI (Dokumentacja interaktywna)
- **URL Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **Specyfikacja OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

*Autoryzacja w Swagger UI:*
1. Otwórz Swagger UI w przeglądarce.
2. Kliknij zielony przycisk **Authorize** w prawym górnym rogu.
3. Wklej uzyskany z Keycloaka `access_token` i kliknij **Authorize**.
4. Testuj endpointy bezpośrednio z poziomu przeglądarki.

##### 2. Przykłady zapytań cURL

- **Pobranie danych pojazdu po numerze VIN (GET):**
  *(Wymagana rola: `ADMIN`, `WLASCICIEL_FIRMY` lub `KIEROWCA`)*
  ```bash
  curl -X GET "http://localhost:8080/truck/YV2RT40A5XA123456" \
    -H "Authorization: Bearer <ACCESS_TOKEN>"
  ```

- **Dodanie nowego pojazdu (POST):**
  *(Wymagana rola: `ADMIN` lub `WLASCICIEL_FIRMY`)*
  ```bash
  curl -X POST "http://localhost:8080/truck" \
    -H "Authorization: Bearer <ACCESS_TOKEN>" \
    -H "Content-Type: application/json" \
    -d '{
      "vendor": "VOLVO",
      "vin": "YV2RT40A5XA123456",
      "plateNumber": "WA 12345",
      "mileage": 150000.5
    }'
  ```

- **Aktualizacja danych pojazdu (PATCH):**
  *(Wymagana rola: `ADMIN` lub `WLASCICIEL_FIRMY`)*
  ```bash
  curl -X PATCH "http://localhost:8080/truck" \
    -H "Authorization: Bearer <ACCESS_TOKEN>" \
    -H "Content-Type: application/json" \
    -d '{
      "vendor": "VOLVO",
      "vin": "YV2RT40A5XA123456",
      "plateNumber": "WA 54321",
      "mileage": 160000.0
    }'
  ```

---

#### 👤 Serwis `drivers` (Port `8081`)

Zarządza danymi kierowców i uprawnieniami (prawami jazdy).

##### Przykłady zapytań cURL

- **Odczyt danych kierowców (GET):**
  *(Wymagana rola: `ADMIN`, `WLASCICIEL_FIRMY` lub `KIEROWCA`)*
  ```bash
  curl -X GET "http://localhost:8081/driver" \
    -H "Authorization: Bearer <ACCESS_TOKEN>"
  ```

- **Operacje zapisu / edycji danych kierowców (POST/PUT/DELETE):**
  *(Wymagana rola: `ADMIN` lub `WLASCICIEL_FIRMY`)*
  ```bash
  curl -X POST "http://localhost:8081/driver" \
    -H "Authorization: Bearer <ACCESS_TOKEN>" \
    -H "Content-Type: application/json" \
    -d '{ ... }'
  ```

- **Sprawdzenie stanu aplikacji (Actuator Health - publiczny):**
  ```bash
  curl -X GET "http://localhost:8081/actuator/health"
  ```
