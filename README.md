# Movieland

Movieland is a simple API that allows you to manage movies, actors, and genres. It provides endpoints to create, update, delete, and retrieve data, as well as track the history of actors’ names across movies. The API is documented with Swagger for easy exploration.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=white&style=flat)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?logo=mongodb&logoColor=white&style=flat)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?logo=swagger&logoColor=black&style=flat)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white&style=flat)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?logo=githubactions&logoColor=white&style=flat)

## Table of Contents

- [Prerequisites](#prerequisites)
- [Running the Application](#running-the-application)
    - [1. Running Locally](#1-running-locally)
    - [2. Running with Docker](#2-running-with-docker)
- [API Documentation](#api-documentation)
- [Testing](#testing)
    - [Integration Tests](#integration-tests)
    - [Modularity Architecture Test](#modularity-architecture-test)
- [Notes](#notes)
- [Example API Requests](#example-api-requests)

## Prerequisites

To run Movieland locally, ensure you have:

- **Java 25** 
- **Gradle 9.1.0** or newer
- **Docker & Docker Compose** (optional if using containerized setup)

## Running the Application

Movieland can be run either **locally** using Gradle or **containerized** using Docker.

### 1. Running Locally

1. Clone the repository:

   ```bash
   git clone <repository-url>
   cd movieland
   ```

2. Make sure MongoDB is running locally on default port `27017`, or set the environment variable `MONGODB_URL`:

   ```bash
   export MONGODB_URL=mongodb://localhost:27017/movieland
   ```

3. Build and run the application:

   ```bash
   ./gradlew bootRun
   ```

4. The API will be accessible at:

   ```
   http://localhost:8080
   ```

### 2. Running with Docker

1. Build and start the containers:

   ```bash
   docker-compose up --build
   ```

2. This will start two services:

    - **app**: Movieland API on `http://localhost:8080`
    - **mongo**: MongoDB on port `27017` with a replica set for distributed transactions

3. To stop and remove containers:

   ```bash
   docker-compose down
   ```

## API Documentation

Swagger UI is available at:

```
http://localhost:8080/swagger-ui/index.html
```

Use it to explore all endpoints, request bodies and response schemas.

> **Note:** The application does not include initial data. You can populate the database via Swagger or API calls.

## Testing

Movieland includes **integration tests** (with Testcontainers for MongoDB) and a **modularity architecture test**.

Run all integration tests with:

```bash
./gradlew test
```

- Controller tests use **RestAssured**
- Service tests use **AssertJ**
- Tests run against a temporary containerized MongoDB, so no local database is needed. Only Docker is required.
- Modularity architecture test validates module boundaries and dependencies using Spring Modullith

> If you run tests in Docker, you can mount the Gradle wrapper and source code, then execute `./gradlew test` inside the container.

## Notes

- All UUIDs in the API (actors, movies, genres) are generated dynamically.
- There is no initial dataset; the database is empty on startup.
- Java 25 is required if running locally.

## Example API Requests

Here are some example `curl` commands you can use to interact with the API. Some may require populating the database:

### Create a Genre

```bash
curl -X POST "http://localhost:8080/genres" \
-H "Content-Type: application/json" \
-d '{"name":"Action"}'
```

### Create a Movie

```bash
curl -X POST "http://localhost:8080/movies" \
-H "Content-Type: application/json" \
-d '{
  "title": "Inception",
  "releaseDate": "2010-07-16",
  "genreId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "actors": [
    {"id":"1a2b3c4d-5678-90ab-cdef-1234567890ab","firstName":"Leonardo","lastName":"DiCaprio"}
  ]
}'
```

### Get a Movie by Title

```bash
curl "http://localhost:8080/movies?title=Inception"
```

### Get Actor History

```bash
curl "http://localhost:8080/snapshot-history/<actor-uuid>"
```

### Check if Actor Changed Name

```bash
curl "http://localhost:8080/snapshot-history/<actor-uuid>/name-change"
```

### Get Actor Name in a Movie

```bash
curl "http://localhost:8080/snapshot-history/movies/<movie-uuid>/actors/<actor-uuid>"
```

---

Movieland is designed to be simple, self-contained, and easy to explore. Use Swagger or the examples above to experiment with creating actors, genres, and movies, and observe the history of actor data changes features in the **History API**.

