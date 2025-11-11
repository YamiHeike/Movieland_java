package com.example.movieland.movie;

import com.example.movieland.actor.Actor;
import com.example.movieland.common.BaseControllerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static com.example.movieland.movie.MovieTestData.getTestActors;
import static com.example.movieland.movie.MovieTestData.getTestMovies;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class MovieControllerTest extends BaseControllerTest {
    @Test
    void returnsMoviePage() {
        // given
        var movies = getTestMovies();
        var pageSize = 2;
        movieService.saveAll(movies);
        // when-then
        given().contentType("application/json")
                .when()
                .get("/movies?size=" + pageSize)
                .then()
                .statusCode(200)
                .body("content", hasSize(2))
                .body("content[0].title", equalTo(movies.getFirst().getTitle()))
                .body("content[0].releaseDate", equalTo(movies.getFirst().getReleaseDate().toString()))
                .body("content[0].genreId", equalTo(movies.getFirst().getGenreId().toString()))
                .body("content[0].actors", hasSize(2))
                .body("content[0].actors[0].id", equalTo(movies.getFirst().getActors().getFirst().id().toString()))
                .body("content[0].actors[1].id", equalTo(movies.getFirst().getActors().getLast().id().toString()))
                .body("content[1].title", equalTo(movies.get(1).getTitle()))
                .body("content[1].releaseDate", equalTo(movies.get(1).getReleaseDate().toString()))
                .body("content[1].genreId", equalTo(movies.get(1).getGenreId().toString()))
                .body("content[1].actors", hasSize(1))
                .body("content[1].actors[0].id", equalTo(movies.get(1).getActors().getFirst().id().toString()));
    }

    @Test
    void getMovieByTitle() {
        // given
        var movie = getTestMovies().get(2);
        movieService.save(movie);
        // when-then
        given().contentType("application/json")
                .when()
                .get("/movies?title=%s".formatted(movie.getTitle()))
                .then()
                .statusCode(200)
                .body("id", equalTo(movie.getId().toString()))
                .body("title", equalTo(movie.getTitle()))
                .body("releaseDate", equalTo(movie.getReleaseDate().toString()))
                .body("genreId", equalTo(movie.getGenreId().toString()))
                .body("actors", hasSize(1))
                .body("actors[0].id", equalTo(movie.getActors().getFirst().id().toString()));
    }

    @Test
    void tryToGetByTitle_whenMovieNotFound() {
        // when-then
        given().contentType("application/json")
                .when()
                .get("/movies?title=%s".formatted(getTestMovies().getFirst().getTitle()))
                .then()
                .statusCode(404);
    }

    @Test
    void getMovieById() {
        // given
        var movie = getTestMovies().get(4);
        movieService.save(movie);
        // when-then
        given().contentType("application/json")
                .when()
                .get("/movies/%s".formatted(movie.getId()))
                .then()
                .statusCode(200)
                .body("id", equalTo(movie.getId().toString()))
                .body("title", equalTo(movie.getTitle()))
                .body("releaseDate", equalTo(movie.getReleaseDate().toString()))
                .body("genreId", equalTo(movie.getGenreId().toString()))
                .body("actors", hasSize(2))
                .body("actors[0].id", equalTo(movie.getActors().getFirst().id().toString()))
                .body("actors[1].id", equalTo(movie.getActors().getLast().id().toString()));
    }

    @Test
    void tryToGetMovieById_whenMovieNotFound() {
        // when-then
        given().contentType("application/json")
                .when()
                .get("/movies/%s".formatted(getTestMovies().getFirst().getId()))
                .then()
                .statusCode(404);
    }

    @Test
    void getMovieByActorId() {
        // given
        var movies = getTestMovies();
        movieService.saveAll(movies);
        var actor = movies.getFirst().getActors().getFirst();
        var expectedMovies = movies.stream()
                .filter(movie -> movie.getActors().contains(actor))
                .toList();
        // when
        given().contentType("application/json")
                .when()
                .get("/movies?actor-id=%s".formatted(actor.id()))
                .then()
                .statusCode(200)
                .body("$.size()", equalTo(expectedMovies.size()));
    }

    @Test
    void createMovie() {
        // given
        var movie = getTestMovies().getLast();
        var actors = getTestActors();
        actors.forEach(actorService::save);
        var createMovieRequest = CreateMovieRequest.of(movie.getTitle(), movie.getReleaseDate(), movie.getGenreId(), movie.getActors());
        // when-then
        given().contentType("application/json")
                .when()
                .body(createMovieRequest)
                .post("/movies")
                .then()
                .statusCode(201)
                .body("title", equalTo(movie.getTitle()))
                .body("releaseDate", equalTo(movie.getReleaseDate().toString()))
                .body("genreId", equalTo(movie.getGenreId().toString()))
                .body("actors", hasSize(2))
                .body("actors[0].id", equalTo(movie.getActors().getFirst().id().toString()))
                .body("actors[1].id", equalTo(movie.getActors().getLast().id().toString()));
        actorCleanup();
    }

    @Test
    void tryToCreateMovie_whenItAlreadyExists() {
        // given
        var movie = getTestMovies().getLast();
        var createMovieRequest = CreateMovieRequest.of(movie.getTitle(), movie.getReleaseDate(), movie.getGenreId(), movie.getActors());
        movieService.save(movie);
        // when-then
        given().contentType("application/json")
                .when()
                .body(createMovieRequest)
                .post("/movies")
                .then()
                .statusCode(400);
    }

    @Test
    void tryToCreateMovie_withNonExistingActor() {
        var movie = getTestMovies().getLast();
        var createMovieRequest = CreateMovieRequest.of(movie.getTitle(), movie.getReleaseDate(), movie.getGenreId(), movie.getActors());
        given().contentType("application/json")
                .when()
                .body(createMovieRequest)
                .post("/movies")
                .then()
                .statusCode(404);
    }

    @Test
    void updateMovie() {
        // given
        var movie = getTestMovies().get(4);
        movieService.save(movie);
        movie.setTitle("Matrix Reloaded");
        // when-then
        given().contentType("application/json")
                .when()
                .body(movie)
                .patch("/movies")
                .then()
                .statusCode(200)
                .body("id", equalTo(movie.getId().toString()))
                .body("title", equalTo("Matrix Reloaded"))
                .body("releaseDate", equalTo(movie.getReleaseDate().toString()))
                .body("genreId", equalTo(movie.getGenreId().toString()))
                .body("actors", hasSize(2))
                .body("actors[0].id", equalTo(movie.getActors().getFirst().id().toString()))
                .body("actors[1].id", equalTo(movie.getActors().getLast().id().toString()));
    }

    @Test
    void tryToUpdateMovie_whenMovieNotFound() {
        // given
        var movie = getTestMovies().getLast();
        // when-then
        given().contentType("application/json")
                .when()
                .body(movie)
                .patch("/movies")
                .then()
                .statusCode(404);
    }

    @AfterEach
    void cleanup() {
        movieService.deleteAll();
    }

    private void actorCleanup() {
        var actorIds = getTestActors().stream()
                .map(Actor::getId)
                .toList();
        actorIds.forEach(actorService::delete);
    }
}
