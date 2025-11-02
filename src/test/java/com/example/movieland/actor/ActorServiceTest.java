package com.example.movieland.actor;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ActorServiceTest {
    @Mock
    private ActorRepository actorRepository;
    @InjectMocks
    private ActorService actorService;
    private static final List<Actor> ACTORS = List.of(
            new Actor(ActorId.generated(), "Tom", "Hanks"),
            new Actor(ActorId.generated(), "Anne", "Hathaway"),
            new Actor(ActorId.generated(), "Leonardo", "DiCaprio")
    );

    @ParameterizedTest
    @MethodSource("providePages")
    void findActorPage(Pageable pageable, List<Actor> expectedActors) {
        // given
        when(actorRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(expectedActors, pageable, ACTORS.size()));
        // when
        var result = actorService.findAll(pageable).getContent();
        // then
        assertThat(result).isEqualTo(expectedActors);
        verify(actorRepository).findAll(pageable);
    }

    static Stream<Arguments> providePages() {
        return Stream.of(
                Arguments.of(PageRequest.of(0, 2), ACTORS.subList(0, 1)),
                Arguments.of(PageRequest.of(1, 2), singletonList(ACTORS.getLast()))
        );
    }
}
