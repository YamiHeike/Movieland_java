package com.example.movieland.movie;

import com.example.movieland.actor.Actor;
import com.example.movieland.actor.ActorNotFound;
import com.example.movieland.actor.ActorService;
import com.example.movieland.common.BaseIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

import static com.example.movieland.movie.MovieTestData.getTestActors;
import static com.example.movieland.movie.MovieTestData.getTestMovies;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class MovieServiceTest extends BaseIntegrationTest {
    @Autowired
    private MovieService movieService;
    @Autowired
    private ActorService actorService;

    @Test
    void findsCorrectMoviePage() {
        // given
        createMovieData();
        var pageSize = 2;
        var expectedTotal = 6;
        var expectedMovies = getTestMovies().subList(0, pageSize);
        // when
        var actualMovies = movieService.getMovies(PageRequest.of(0, pageSize));
        // then
        assertThat(actualMovies).satisfies(movies -> {
            assertThat(movies.getTotalElements()).isEqualTo(expectedTotal);
            assertThat(actualMovies.getContent())
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expectedMovies);
        });
    }

    @Test
    void findMovieById_whenItExists() {
        // given
        var expectedMovie = getTestMovies().getLast();
        movieService.save(expectedMovie);
        // when
        var actualMovie = movieService.getById(expectedMovie.getId());
        // then
        assertThat(actualMovie)
                .usingRecursiveComparison()
                .isEqualTo(expectedMovie);
    }

    @Test
    void throwException_whenMovieIdNotFound() {
        // when-then
        assertThatThrownBy(() -> movieService.getById(UUID.randomUUID()))
                .isExactlyInstanceOf(MovieNotFound.class);
    }

    @Test
    void findMovieByTitle_whenItExists() {
        // given
        var expectedMovie = getTestMovies().getFirst();
        movieService.save(expectedMovie);
        // when
        var actualMovie = movieService.getByTitle(expectedMovie.getTitle());
        // then
        assertThat(actualMovie)
                .usingRecursiveComparison()
                .isEqualTo(expectedMovie);
    }

    @Test
    void throwException_whenMovieTitleNotFound() {
        assertThatThrownBy(() -> movieService.getByTitle("test"))
                .isExactlyInstanceOf(MovieNotFound.class);
    }

    @Test
    void findByActorId() {
        // given
        var movies = getTestMovies();
        movieService.saveAll(movies);
        var actor = movies.getFirst().getActors().getFirst();
        var expectedMovies = movies.stream()
                .filter(movie -> movie.getActors().contains(actor))
                .toList();
        // when
        var actualMovies = movieService.getByActorId(actor.id());
        // then
        assertThat(actualMovies).isEqualTo(expectedMovies);
    }

    @Test
    void createMovie() {
        // given
        var expectedMovie = getTestMovies().get(4);
        var testActors = getTestActors();
        testActors.forEach(actorService::save);
        var createMovieRequest = CreateMovieRequest.of(expectedMovie.getTitle(),
                expectedMovie.getReleaseDate(),
                expectedMovie.getGenreId(),
                expectedMovie.getActors());
        // when
        var actualMovie = movieService.create(createMovieRequest);
        // then
        assertThat(actualMovie)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedMovie);
        actorsCleanup();
    }

    @Test
    void throwException_whenCreatingAlreadyExistingMovie() {
        // given
        var expectedMovie = getTestMovies().get(4);
        var testActors = getTestActors();
        testActors.forEach(actorService::save);
        movieService.save(expectedMovie);
        var createMovieRequest = CreateMovieRequest.of(expectedMovie.getTitle(),
                expectedMovie.getReleaseDate(),
                expectedMovie.getGenreId(),
                expectedMovie.getActors());
        // when-then
        assertThatThrownBy(() -> movieService.create(createMovieRequest))
                .isExactlyInstanceOf(MovieAlreadyExists.class);
        actorsCleanup();
    }

    @Test
    void throwException_whenCreatingMovieWithNotExistingActor() {
        // given
        var movie = getTestMovies().get(4);
        var createMovieRequest = CreateMovieRequest.of(
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getGenreId(),
                movie.getActors()
        );
        // when-then
        assertThatThrownBy(() -> movieService.create(createMovieRequest))
                .isExactlyInstanceOf(ActorNotFound.class);
    }

    @Test
    void updatesMovie() {
        // given
        var actors = getTestActors();
        actorService.saveAll(actors);
        var expectedMovie = getTestMovies().getFirst();
        var previousTitle = expectedMovie.getTitle();
        movieService.save(expectedMovie);
        expectedMovie.setTitle("Matrix");
        // when
        var actualMovie = movieService.update(expectedMovie);
        assertThat(actualMovie).isEqualTo(expectedMovie);
        assertThatThrownBy(() -> movieService.getByTitle(previousTitle))
                .isExactlyInstanceOf(MovieNotFound.class);
    }

    @Test
    void throwException_whenUpdatingMovieNotFound() {
        assertThatThrownBy(() -> movieService.update(getTestMovies().getFirst()))
                .isExactlyInstanceOf(MovieNotFound.class);
    }

    @Test
    void deleteMovie() {
        // given
        var expectedMovie = getTestMovies().get(5);
        movieService.save(expectedMovie);
        // when
        movieService.delete(expectedMovie.getId());
        // then
        assertThatThrownBy(() -> movieService.getById(expectedMovie.getId()))
                .isExactlyInstanceOf(MovieNotFound.class);
    }

    @Test
    void throwException_whenDeletingMovieNotFound() {
        // given
        assertThatThrownBy(() -> movieService.delete(UUID.randomUUID()))
                .isExactlyInstanceOf(MovieNotFound.class);
    }

    void createMovieData() {
        movieService.saveAll(getTestMovies());
    }


    @AfterEach
    void cleanup() {
        movieService.deleteAll();
    }

    private void actorsCleanup() {
        var actorIds = getTestActors().stream()
                .map(Actor::getId)
                .toList();
        actorIds.forEach(actorService::delete);
    }
}
