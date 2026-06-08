# 🏞️ NPS Parks API

A Spring Boot REST API that ingests data from the **U.S. National Park Service (NPS) API**,
persists it to PostgreSQL, and serves it back through a clean, documented JSON API. On startup the
app pulls parks (with their activities, topics, entrance fees, and images) from the NPS feed, maps
the responses into JPA entities, and stores them — so queries are served from your own database
rather than hitting the upstream API on every request.

## Tech stack

| Layer | Technologies |
|-------|--------------|
| Language | Java 21 |
| Framework | Spring Boot 4.0 (Web MVC, Data JPA, Security, Validation) |
| Database | PostgreSQL (Spring Data JPA / Hibernate) |
| API docs | springdoc-openapi / Swagger UI |
| Build | Maven (wrapper included) |

## What it does

1. **Ingest** — on startup, `NpsDataLoader` (a `CommandLineRunner`) calls `NpsApiService`, which
   fetches parks from `https://developer.nps.gov/api/v1` using your API key.
2. **Map** — the NPS JSON (`NpsResponse` → `ParkDto`, etc.) is mapped into JPA entities:
   `Park`, `Activity`, `Topic`, `EntranceFee`, and `ParkImage`.
3. **Persist** — entities are saved to PostgreSQL via Spring Data repositories.
4. **Serve** — `ParkController` exposes the data as a REST API, documented with Swagger.

## REST API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/parks` | All parks. Optional `?state=PA` to filter by state. |
| GET | `/api/parks/{parkCode}` | A single park by its NPS park code (e.g. `yose`). |
| GET | `/api/parks/free` | Parks with no entrance fee. |

- **Swagger UI:** <http://localhost:8080/swagger-ui.html>
- **OpenAPI JSON:** `/v3/api-docs`

## Project structure

```
com.example.demo
├─ config/      NpsApiProperties (@ConfigurationProperties), SecurityConfig
├─ dto/         NpsResponse, ParkDto, ActivityDto, TopicDto, EntranceFeeDto, ImageDto
├─ entity/      Park, Activity, Topic, EntranceFee, ParkImage
├─ repository/  ParkRepository, ActivityRepository, TopicRepository
├─ service/     NpsApiService (fetch), ParkService (query)
├─ runner/      NpsDataLoader (CommandLineRunner — seeds the DB on startup)
└─ controller/  ParkController
```

## Running locally

**Prerequisites:** Java 21, a running PostgreSQL instance, and a free
[NPS API key](https://www.nps.gov/subjects/developer/get-started.htm).

1. Create the database, e.g. `nps`.
2. Add **`src/main/resources/application-local.properties`** (git-ignored — keep your secrets out
   of version control):
   ```properties
   nps.api.key=YOUR_NPS_API_KEY
   spring.datasource.url=jdbc:postgresql://localhost:5432/nps
   spring.datasource.username=YOUR_DB_USER
   spring.datasource.password=YOUR_DB_PASSWORD
   ```
3. Run with the `local` profile active:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
   ```
4. On first run the app populates the database from the NPS API. Then browse the data at
   <http://localhost:8080/swagger-ui.html> or hit the endpoints directly, e.g.
   `GET http://localhost:8080/api/parks?state=PA`.
