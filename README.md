# URL Shortener API

A production-grade URL shortener built with Kotlin and Spring Boot.
Supports JWT authentication, Redis caching, and click analytics.

## Tech Stack

- Kotlin + Spring Boot → REST API
- PostgreSQL (Supabase) → persistent storage
- Redis → caching layer for fast redirects
- JWT → stateless authentication
- JUnit 5 + Mockk → unit testing

## Architecture Decisions

- Used 302 redirect instead of 301 to track every click for analytics
- Used Redis in front of PostgreSQL to reduce DB load on redirects
- Stored clicks in separate table for detailed analytics (device, date)
- Used stateless JWT instead of sessions for scalability
- Short codes generated with collision detection using do-while loop

## API Endpoints

**Auth**
- POST /api/auth/register → register new user
- POST /api/auth/login → login and get JWT token

**URL**
- POST /api/url/shorten → create short URL
- GET /api/url/urls → get all my URLs
- DELETE /api/url/{shortCode} → delete a URL
- GET /api/url/{shortCode}/analytics/devices → clicks by device
- GET /api/url/{shortCode}/analytics/dates → clicks by date

**Redirect**
- GET /{shortCode} → redirects to original URL (302)

## Environment Variables

- DATABASE_URL
- DATABASE_USERNAME
- DATABASE_PASSWORD
- REDIS_HOST
- REDIS_PORT
- REDIS_PASSWORD
- JWT_SECRET
- JWT_EXPIRATION

## How to Run Locally

- Clone the repo
- Set environment variables in IntelliJ run config
- Run with green play button or ./gradlew bootRun

## Tests

- UserService → register success, duplicate username, login success, wrong password
- UrlService → shorten URL, Redis cache hit, Redis cache miss with DB fallback

Run tests:
- ./gradlew test
