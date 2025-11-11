package com.example.movieland.movie;

import com.example.movieland.common.BaseIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

import static com.example.movieland.movie.MovieTestData.getTestMovies;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class MovieServiceTest extends BaseIntegrationTest {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieService movieService;

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
        movieRepository.save(expectedMovie);
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
        movieRepository.save(expectedMovie);
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
        movieRepository.saveAll(movies);
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
    }

    @Test
    void throwException_whenCreatingAlreadyExistingMovie() {
        // given
        var expectedMovie = getTestMovies().get(4);
        movieRepository.save(expectedMovie);
        var createMovieRequest = CreateMovieRequest.of(expectedMovie.getTitle(),
                expectedMovie.getReleaseDate(),
                expectedMovie.getGenreId(),
                expectedMovie.getActors());
        // when-then
        assertThatThrownBy(() -> movieService.create(createMovieRequest))
                .isExactlyInstanceOf(MovieAlreadyExists.class);
    }

    @Test
    void updatesMovie() {
        // given
        var expectedMovie = getTestMovies().getFirst();
        var previousTitle = expectedMovie.getTitle();
        movieRepository.save(expectedMovie);
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
        movieRepository.save(expectedMovie);
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
        movieRepository.saveAll(getTestMovies());
    }


    @AfterEach
    void cleanup() {
        movieRepository.deleteAll();
    }
}
