package com.example.movieland.movie.history;

import com.example.movieland.common.BaseIntegrationTest;
import com.example.movieland.movie.ActorSnapshot;
import com.example.movieland.movie.MovieService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.example.movieland.movie.MovieTestData.getTestMovies;
import static com.example.movieland.movie.history.HistoryTestDataSupplier.*;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class HistoryServiceTest extends BaseIntegrationTest {
    @Autowired
    private MovieService movieService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private HistoryTestDataSupplier testDataSupplier;

    @Test
    void findActorNameChangeHistory() {
        // given
        testDataSupplier.prepareDataSet();
        var movie = getTestMovies().get(CHANGED_MOVIE_IDX);
        var snapshot = movie.getActors().get(CHANGED_ACTOR_IDX);
        var expectedHistory = ActorHistoryResponse.of(snapshot.id(), List.of(
                ActorDetailsResponse.of(snapshot.firstName(), snapshot.lastName()),
                ActorDetailsResponse.of(CHANGED_NAME, snapshot.lastName())
        ));
        // when
        var actualHistory = historyService.getActorDataChangeHistory(snapshot.id());
        // then
        assertThat(actualHistory)
                .usingRecursiveComparison()
                .isEqualTo(expectedHistory);
    }

    @Test
    void returnEntryWithEmptyList_whenActorHasNoHistory() {
        // given
        var id = UUID.randomUUID();
        var expectedEntries = ActorHistoryResponse.of(id, emptyList());
        // when
        var actualEntries = historyService.getActorDataChangeHistory(id);
        // then
        assertThat(actualEntries).isEqualTo(expectedEntries);
    }

    @Test
    void getActorNameInMovie() {
        // given
        var movieId = testDataSupplier.prepareDataSet();
        var actor = getTestMovies().get(CHANGED_MOVIE_IDX).getActors().get(CHANGED_ACTOR_IDX);
        var expectedName = "%s %s".formatted(CHANGED_NAME, actor.lastName());
        // when
        var response = historyService.getActorNameInMovie(movieId, actor.id());
        var actualName = "%s %s".formatted(response.firstName(), response.lastName());
        // then
        assertThat(actualName).isEqualTo(expectedName);
    }

    @Test
    void throwException_whenActorNotCastInMovie() {
        // given
        var movieId = testDataSupplier.prepareDataSet();
        var actorId = getTestMovies().get(2).getActors().getFirst().id();
        // when-then
        assertThatThrownBy(() -> historyService.getActorNameInMovie(movieId, actorId))
                .isExactlyInstanceOf(ActorNotCastInMovie.class);
    }

    @ParameterizedTest
    @MethodSource
    void confirmActorDataChange(UUID actorId, boolean expectedResult) {
        // given
        testDataSupplier.prepareDataSet();
        // when
        var actualResult = historyService.didActorChangedDataOverTime(actorId);
        // then
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    static Stream<Arguments> confirmActorDataChange() {
        var changedMovieActors = getTestMovies().get(CHANGED_MOVIE_IDX).getActors();
        var changedActorId = changedMovieActors.get(CHANGED_ACTOR_IDX).id();
        var notChangedActorId = changedMovieActors.stream()
                .map(ActorSnapshot::id)
                .filter(id -> !id.equals(changedActorId))
                .findFirst().orElse(UUID.randomUUID());
        return Stream.of(
                Arguments.of(changedActorId, true),
                Arguments.of(notChangedActorId, false)
        );
    }

    @AfterEach
    void cleanup() {
        movieService.deleteAll();
    }
}
