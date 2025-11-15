package com.example.movieland.history;

import com.example.movieland.common.BaseControllerTest;
import com.example.movieland.movie.MovieService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.movieland.movie.MovieTestData.getTestMovies;
import static com.example.movieland.history.HistoryTestDataSupplier.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class HistoryControllerTest extends BaseControllerTest {
    @Autowired
    private MovieService movieService;
    @Autowired
    private HistoryTestDataSupplier dataSupplier;

    @Test
    void returnActorHistory() {
        // given
        dataSupplier.prepareDataSet();
        var actorId = getTestMovies().get(CHANGED_MOVIE_IDX).getActors().get(CHANGED_ACTOR_IDX).id();
        // when-then
        given().contentType("application/json")
                .when()
                .get("/snapshot-history/%s".formatted(actorId))
                .then()
                .statusCode(200)
                .body("id", equalTo(actorId.toString()))
                .body("entries.size()", equalTo(2));
    }

    @Test
    void changeIfActorChangedName() {
        // given
        dataSupplier.prepareDataSet();
        var actorId = getTestMovies().get(CHANGED_MOVIE_IDX).getActors().get(CHANGED_ACTOR_IDX).id();
        // when-then
        given().contentType("application/json")
                .when()
                .get("/snapshot-history/%s/name-change".formatted(actorId))
                .then()
                .statusCode(200)
                .body("$", equalTo(true));
    }

    @Test
    void returnActorNameInMovie() {
        // given
        var movieId = dataSupplier.prepareDataSet();
        var actorId = getTestMovies().get(CHANGED_MOVIE_IDX).getActors().get(CHANGED_ACTOR_IDX).id();
        // when-then
        given().contentType("application/json")
                .when()
                .get("/snapshot-history/movies/%s/actors/%s".formatted(movieId, actorId))
                .then()
                .statusCode(200)
                .body("firstName", equalTo(CHANGED_NAME));
    }

    @AfterEach
    void cleanup() {
        movieService.deleteAll();
    }
}
