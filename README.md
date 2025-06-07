# Course Management App

## ✅ Technologie
- Java 21
- Spring Boot 3.x
- PostgreSQL
- Angular 17 (Standalone Components)
- Docker + Docker Compose

## 🔧 Wymagania
- Docker & Docker Compose
- Java 21
- Node.js + npm (v18+)

## 🚀 Jak uruchomić?

### Backend (Spring Boot)
```bash
docker-compose up --build
```

### Frontend (Angular)
```bash
cd frontend
npm install
npm start
```

API: http://localhost:8080/api/courses
Frontend: http://localhost:4200
Swagger: http://localhost:8080/swagger-ui.html

## 🔐 Zmienne środowiskowe
.env plik wspiera backend i frontend:
- BACKEND_PORT=8080
- POSTGRES_USER=postgres
- POSTGRES_PASSWORD=postgres
- DB_NAME=courses
- ANGULAR_API_URL=http://localhost:8080/api

## 🧪 Testowanie
- Możliwość dodania testów jednostkowych w `src/test` (np. JUnit + Mockito)
- Frontend testy przez `ng test`

## ✨ Bonusy (opcjonalne)
- Duplikowanie kursu
- Średnia długość kursów
- Filtrowanie po dacie publikacji