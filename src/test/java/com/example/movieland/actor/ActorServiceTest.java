package com.example.movieland.actor;

import com.example.movieland.common.BaseIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ActorServiceTest extends BaseIntegrationTest {
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private ActorService actorService;
    private final List<Actor> actors = List.of(
            Actor.from("Leonardo", "DiCaprio", LocalDate.of(1974,11,11)),
            Actor.from("Natalie", "Portman", LocalDate.of(1981, 6, 9)),
            Actor.from("Denzel", "Washington", LocalDate.of(1954, 12, 28)),
            Actor.from("Scarlett", "Johansson", LocalDate.of(1984, 11, 22)),
            Actor.from("Christian", "Bale", LocalDate.of(1974, 1, 30))
    );

    @Test
    void returnsCorrectActor() {
        // given
        var expectedActor = Actor.from("Anne", "Hathaway", LocalDate.of(1982, 11, 12));
        actorService.save(expectedActor);

        // when
        var actualActor = actorService.findByFirstNameAndLastName("Anne", "Hathaway");

        // then
        assertThat(actualActor).isEqualTo(expectedActor);
    }

    @Test
    void returnsCorrectPage() {
        // given
        actors.forEach(actorService::save);
        var pageable = PageRequest.of(0, 3);
        var expectedActors = actors.subList(0, 3);

        // when
        var actorsPage = actorService.findAll(pageable);

        // then
        assertThat(expectedActors).isEqualTo(actorsPage.getContent());
        assertThat(actorsPage.getTotalElements()).isEqualTo(actors.size());
    }

    @Test
    void createNewActor() {
        // given
        var expectedActor = actors.getLast();
        // when
        var actualActor = actorService.createActor(CreateActorRequest.of(expectedActor.getFirstName(), expectedActor.getLastName(), expectedActor.getBirthDate()));
        // then
        assertThat(actualActor)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedActor);
    }

    @Test
    void updateActor() {
        // given
        var actor = Actor.from("Mark", "Sinclair", LocalDate.of(1967, 7, 18));
        actorService.save(actor);
        var expectedActor = Actor.fromRequest(actor.getId(), new CreateActorRequest("Vin", "Diesel", actor.getBirthDate()));
        // when
        actorService.update(expectedActor, false);
        var actualActor = actorService.findById(actor.getId());
        // then
        assertThat(actualActor).isEqualTo(expectedActor);
        assertThatThrownBy(() -> actorService.findByFirstNameAndLastName(actor.getFirstName(), actor.getLastName()))
                .isExactlyInstanceOf(ActorNotFound.class);
    }

    @Test
    void deleteActor() {
        // given
        var actor = actors.get(3);
        actorService.save(actor);
        // when
        actorService.delete(actor.getId());
        // then
        assertThatThrownBy(() -> actorService.findById(actor.getId()))
                .isExactlyInstanceOf(ActorNotFound.class);
    }

    @Test
    void throwException_whenActorNotFound() {
        // given
        var firstName = "Tom";
        var lastName = "Hanks";
        // when-then
        assertThatThrownBy(() -> actorService.findByFirstNameAndLastName(firstName, lastName)).isExactlyInstanceOf(ActorNotFound.class);
    }

    @ParameterizedTest
    @MethodSource("invalidParams")
    void throwException_whenNull(String firstName, String lastName) {
        assertThatThrownBy(() -> actorService.findByFirstNameAndLastName(firstName, lastName)).isExactlyInstanceOf(NullPointerException.class);
    }

    static Stream<Arguments> invalidParams() {
        return Stream.of(
                Arguments.of("Leonardo", null),
                Arguments.of(null, "DiCaprio"),
                Arguments.of(null, null)
        );
    }

    @Test
    void throwException_whenCreatingAlreadyExistingActor() {
        // given
        var expectedActor = actors.getLast();
        var createActorRequest = CreateActorRequest.of(
                expectedActor.getFirstName(), expectedActor.getLastName(), expectedActor.getBirthDate());
        actorService.save(expectedActor);

        // when-then
        assertThatThrownBy(() -> actorService.createActor(createActorRequest)).isExactlyInstanceOf(ActorAlreadyExists.class);
    }

    @Test
    void throwException_whenUpdatingExistingActor() {
        // given
        var actor = actors.getFirst();
        // when-then
        assertThatThrownBy(() -> actorService.update(actor, false)).isExactlyInstanceOf(ActorNotFound.class);
    }

    @Test
    void throwException_whenDeletingNotExistingActor() {
        var actor = actors.getFirst();
        assertThatThrownBy(() -> actorService.delete(actor.getId()))
                .isExactlyInstanceOf(ActorNotFound.class);
    }

    @AfterEach
    void cleanup() {
        actorRepository.deleteAll();
    }

}
