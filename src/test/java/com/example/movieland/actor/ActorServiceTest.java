package com.example.movieland.actor;

import com.example.movieland.common.BaseIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ActorServiceTest extends BaseIntegrationTest {
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private ActorService actorService;

    @Test
    void returnsCorrectActor() {
        // given
        var expectedActor = Actor.from("Anne", "Hathaway", LocalDate.of(1982, 11, 12));
        actorRepository.save(expectedActor);

        // when
        var actualActor = actorService.findByFirstNameAndLastName("Anne", "Hathaway");

        // then
        assertThat(actualActor).isEqualTo(expectedActor);
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

    @AfterEach
    void cleanup() {
        actorRepository.deleteAll();
    }

}
