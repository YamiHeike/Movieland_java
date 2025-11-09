package com.example.movieland.movie;

import com.example.movieland.common.BaseIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class MovieServiceTest extends BaseIntegrationTest {
    private final static UUID ACTION_UUID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private final static UUID SCI_FI_UUID  = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private final static ActorSnapshot KEANU = ActorSnapshot.from(UUID.randomUUID(), "Keanu", "Reeves");
    private final static ActorSnapshot CARRIE = ActorSnapshot.from(UUID.randomUUID(), "Carrie-Anne", "Moss");
    private final static ActorSnapshot LEO = ActorSnapshot.from(UUID.randomUUID(), "Leonardo", "DiCaprio");
    private final static ActorSnapshot MATTHEW = ActorSnapshot.from(UUID.randomUUID(), "Matthew", "McConaughey");
    private final static ActorSnapshot ANNE = ActorSnapshot.from(UUID.randomUUID(), "Anne", "Hathaway");
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

    List<Movie> getTestMovies() {
        Movie m1 = Movie.of(
                "The Matrix",
                LocalDate.of(1999, 3, 31),
                SCI_FI_UUID,
                List.of(KEANU, CARRIE)
        );
        Movie m2 = Movie.of(
                "John Wick",
                LocalDate.of(2014, 10, 24),
                ACTION_UUID,
                List.of(KEANU)
        );
        Movie m3 = Movie.of(
                "Inception",
                LocalDate.of(2010, 7, 16),
                SCI_FI_UUID,
                List.of(LEO)
        );
        Movie m4 = Movie.of(
                "Interstellar",
                LocalDate.of(2014, 11, 7),
                SCI_FI_UUID,
                List.of(MATTHEW, ANNE)
        );
        Movie m5 = Movie.of(
                "The Matrix Reloaded",
                LocalDate.of(2003, 5, 15),
                SCI_FI_UUID,
                List.of(KEANU, CARRIE)
        );
        Movie m6 = Movie.of(
                "The Matrix Revolutions",
                LocalDate.of(2003, 11, 5),
                SCI_FI_UUID,
                List.of(KEANU, CARRIE)
        );
        return List.of(m1, m2, m3, m4, m5, m6);
    }

    @AfterEach
    void cleanup() {
        movieRepository.deleteAll();
    }
}
