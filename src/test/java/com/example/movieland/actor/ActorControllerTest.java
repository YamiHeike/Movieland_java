package com.example.movieland.actor;

import com.example.movieland.common.BaseControllerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ActorControllerTest extends BaseControllerTest {
    private final List<CreateActorRequest> createActorRequests = List.of(
            CreateActorRequest.of("Leonardo", "DiCaprio", LocalDate.of(1974,11,11)),
            CreateActorRequest.of("Natalie", "Portman", LocalDate.of(1981, 6, 9))
    );

    @Test
    void findAuthorPage() {
        // given
        createActorRequests.forEach(actorService::createActor);
        //when-then
        given().contentType("application/json")
                .when()
                .get("/actors")
                .then()
                .statusCode(200)
                .body("content", hasSize(2))
                .body("content[0].firstName", equalTo("Leonardo"))
                .body("content[0].lastName", equalTo("DiCaprio"))
                .body("content[0].birthDate", equalTo("1974-11-11"))
                .body("content[1].firstName", equalTo("Natalie"))
                .body("content[1].lastName", equalTo("Portman"))
                .body("content[1].birthDate", equalTo("1981-06-09"));
    }

    @Test
    void findActorById() {
        // given
        var actor = actorService.createActor(createActorRequests.getFirst());
        // when-then
        given().contentType("application/json")
                .when()
                .get("/actors/%s".formatted(actor.getId()))
                .then()
                .statusCode(200)
                .body("firstName", equalTo("Leonardo"))
                .body("lastName", equalTo("DiCaprio"))
                .body("birthDate", equalTo("1974-11-11"));
    }

    @Test
    void createActor() {
        // given
        var createActorRequest =
                CreateActorRequest.of("Leonardo", "DiCaprio", LocalDate.of(1974,11,11));

        // when-then
        given().contentType("application/json")
                .when()
                .body(createActorRequest)
                .post("/actors")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("firstName", equalTo("Leonardo"))
                .body("lastName", equalTo("DiCaprio"))
                .body("birthDate", equalTo("1974-11-11"));
    }

    @Test
    void tryToCreateExistingActor() {
        var actor = actorService.createActor(createActorRequests.getFirst());
        given().contentType("application/json")
                .when()
                .body(actor)
                .post("/actors")
                .then()
                .statusCode(400);
    }

    @Test
    void updateActor() {
        // given
        var createActorRequest =
                CreateActorRequest.of("Natali", "Portman", LocalDate.of(1981,6,9));
        actorService.createActor(createActorRequest);
        var actor = actorService.findByFirstNameAndLastName("Natali", "Portman");
        actor.setFirstName("Natalie");

        // when-then
        given().contentType("application/json")
                .when()
                .body(actor)
                .patch("/actors")
                .then()
                .statusCode(200)
                .body("firstName", equalTo("Natalie"))
                .body("lastName", equalTo("Portman"))
                .body("birthDate", equalTo("1981-06-09"));
        given().contentType("application/json")
                .when()
                .get("/actors/%s".formatted(actor.getId()))
                .then()
                .statusCode(200)
                .body("firstName", equalTo("Natalie"));
    }

    @Test
    void tryToUpdateActorThatDoesNotExist() {
        given().contentType("application/json")
                .when()
                .body(Actor.fromRequest(UUID.randomUUID(), createActorRequests.getFirst()))
                .patch("/actors")
                .then()
                .statusCode(404);
    }

    @Test
    void deleteActor() {
        var createActorRequest = createActorRequests.getLast();
        var actor = actorService.createActor(createActorRequest);
        given().contentType("application/json")
                .when()
                .delete("/actors/%s".formatted(actor.getId()))
                .then()
                .statusCode(204);
        given().contentType("application/json")
                .when()
                .get("/actors/%s".formatted(actor.getId()))
                .then()
                .statusCode(404);
    }

    @Test
    void tryToDeleteActorThatDoesNotExist() {
        given().contentType("application/json")
                .when()
                .delete("/actors/%s".formatted(UUID.randomUUID()))
                .then()
                .statusCode(404);
    }

    @AfterEach
    void cleanup() {
        actorService.deleteAll();
    }
}
