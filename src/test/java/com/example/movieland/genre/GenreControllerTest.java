package com.example.movieland.genre;

import com.example.movieland.common.BaseControllerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GenreControllerTest extends BaseControllerTest {
    @Autowired
    private GenreService genreService;
    @Autowired
    private GenreRepository genreRepository;

    private final List<CreateGenreRequest> createGenreRequests = List.of(
            CreateGenreRequest.of("Action"),
            CreateGenreRequest.of("Fantasy"),
            CreateGenreRequest.of("Sci-fi"),
            CreateGenreRequest.of("Documentary")
    );

    @Test
    void returnAllGenres() {
        // given
        createGenreRequests.forEach(genreService::createGenre);
        // when-then
        given().contentType("application/json")
                .when()
                .get("/genres")
                .then()
                .statusCode(200)
                .body("[0].name", equalTo("Action"))
                .body("[1].name", equalTo("Fantasy"))
                .body("[2].name", equalTo("Sci-fi"))
                .body("[3].name", equalTo("Documentary"))
                .body("$.size()", equalTo(4));
    }

    @Test
    void findById() {
        // given
        var createdGenre = genreService.createGenre(createGenreRequests.getFirst());
        // when-then
        given().contentType("application/json")
                .when()
                .get("/genres/{id}", createdGenre.getId())
                .then()
                .statusCode(200)
                .body("name", equalTo(createdGenre.getName()));
    }

    @Test
    void createGenre() {
        // given
        var request = createGenreRequests.get(3);
        //when-then
        given().contentType("application/json")
                .when()
                .body(request)
                .post("/genres")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo(request.name()));
    }

    @Test
    void tryToCreateExistingGenre() {
        var createdGenre = genreService.createGenre(createGenreRequests.getFirst());
        given().contentType("application/json")
                .when()
                .body(createdGenre)
                .post("/genres")
                .then()
                .statusCode(400);
    }

    @Test
    void updateGenre() {
        // given
        var createdGenre = genreService.createGenre(CreateGenreRequest.of("Fantas"));
        var correctName = "Fantasy";
        createdGenre.setName(correctName);
        // when-then
        given().contentType("application/json")
                .when()
                .body(createdGenre)
                .patch("/genres")
                .then()
                .statusCode(200)
                .body("name", equalTo(correctName));
        given().contentType("application/json")
                .when()
                .get("/genres/{id}", createdGenre.getId())
                .then()
                .statusCode(200)
                .body("name", equalTo(correctName));
    }

    @Test
    void tryToUpadateNotExistingGenre() {
        // given
        var genreId = UUID.randomUUID();
        // when-then
        given().contentType("application/json")
                .when()
                .body(Genre.fromRequest(genreId, createGenreRequests.get(2)))
                .patch("/genres")
                .then()
                .statusCode(404);
    }

    @Test
    void deleteGenre() {
        // given
        var genre = genreService.createGenre(createGenreRequests.get(3));
        // when-then
        given().contentType("application/json")
                .when()
                .delete("/genres/{id}", genre.getId())
                .then()
                .statusCode(204);
        given().contentType("application/json")
                .when()
                .get("/genres/{id}", genre.getId())
                .then()
                .statusCode(404);
    }

    @Test
    void tryToDeleteNotExistingGenre() {
        // when-then
        given().contentType("application/json")
                .when()
                .delete("/genres/{id}", UUID.randomUUID())
                .then()
                .statusCode(404);
    }

    @AfterEach
    void cleanup() {
        genreRepository.deleteAll();
    }

}
