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

### 2. Uruchomienie infrastruktury (Docker)
W katalogu głównym projektu uruchom kontenery bazodanowe oraz Keycloak:

```bash
docker compose up -d
```

W ramach `docker-compose.yaml` uruchamiane są następujące usługi:
- **Keycloak** (OAuth2 / OIDC): `http://localhost:8180` (realm `truckmanager` jest automatycznie importowany przy starcie)
- **PostgreSQL**: `localhost:5432` (bazy `trucksdb` oraz `keycloakdb`, użytkownik: `root`, hasło: `example`)
- **MongoDB**: `localhost:27017` (użytkownik: `root`, hasło: `example`)
- **Mongo Express** (panel WWW do MongoDB): `http://localhost:8081` (login: `test`, hasło: `test`)

---

### 3. Uruchomienie serwisów Spring Boot

Aplikacja składa się z dwóch mikrousług:

#### Serwis `trucks` (port `8080`)
Zarządza danymi pojazdów (MongoDB).
```bash
# W katalogu trucks/
mvn spring-boot:run
```
*Domyślne zmienne środowiskowe:*
- `MONGO_DB_URL=mongodb://root:example@localhost:27017/trucks?authSource=admin`
- `KEYCLOAK_ISSUER_URI=http://localhost:8180/realms/truckmanager`

#### Serwis `drivers` (port `8081`)
Zarządza danymi kierowców (PostgreSQL + Flyway).
```bash
# W katalogu drivers/
mvn spring-boot:run
```

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

### 3. Autoryzacja w zapytaniach do API

#### cURL / HTTP Request:
Wysyłając zapytania do endpointów API, przekaż token w nagłówku `Authorization`:
```bash
curl -X GET "http://localhost:8080/trucks" \
  -H "Authorization: Bearer <TUTAJ_WKLEJ_ACCESS_TOKEN>"
```

#### Swagger UI (serwis `trucks`):
1. Otwórz w przeglądarce: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
2. Kliknij zielony przycisk **Authorize** w prawym górnym rogu.
3. Wklej pobrany token JWT (sam token lub z prefiksem `Bearer ` w zależności od formatu) i kliknij **Authorize**.
4. Wykonuj autoryzowane zapytania bezpośrednio z poziomu interfejsu Swagger.
