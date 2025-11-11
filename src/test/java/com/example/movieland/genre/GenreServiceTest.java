package com.example.movieland.genre;

import com.example.movieland.common.BaseIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class GenreServiceTest extends BaseIntegrationTest {
    @Autowired
    private GenreService genreService;

    @Test
    void returnsAllGenres() {
        // given
        var expectedGenres = List.of(Genre.of("Documentary"), Genre.of("Thriller"), Genre.of("Action"));
        createTestData(expectedGenres);
        // when-then
        assertThat(genreService.findAll()).isEqualTo(expectedGenres);
    }

    @Test
    void findGenreByName() {
        // given
        var expectedGenre = Genre.of("Historical");
        createTestData(singletonList(expectedGenre));
        // when
        var actualGenre = genreService.findByName(expectedGenre.getName());
        // then
        assertThat(actualGenre).isEqualTo(expectedGenre);
    }

    @Test
    void updateGenre() {
        // given
        var expectedGenres = List.of(Genre.of("Thriller"), Genre.of("Action"));
        createTestData(expectedGenres);
        var genreToUpdate = expectedGenres.getFirst();
        var previousGenreName = genreToUpdate.getName();
        genreToUpdate.setName("Horror");
        // when
        var updatedGenre = genreService.updateGenre(genreToUpdate);
        // then
        assertThat(updatedGenre).satisfies(actualGenre -> {
            assertThat(updatedGenre).isEqualTo(genreToUpdate);
            assertThat(actualGenre.getName()).isNotEqualTo(previousGenreName);
        });
        assertThatThrownBy(() -> genreService.findByName(previousGenreName))
                .isExactlyInstanceOf(GenreNotFound.class);
    }

    @Test
    void createGenre() {
        // given
        var request = CreateGenreRequest.of("Drama");
        // when
        var actualGenre = genreService.createGenre(request);
        var expectedGenre = Genre.fromRequest(actualGenre.getId(), request);
        // then
        assertThat(actualGenre).isEqualTo(expectedGenre);
    }

    @Test
    void throwException_whenCreatingExistingGenre() {
        // given
        var request = CreateGenreRequest.of("Historical");
        genreService.createGenre(request);
        // when-then
        assertThatThrownBy(() -> genreService.createGenre(request))
                .isExactlyInstanceOf(GenreAlreadyExists.class);
    }

    @Test
    void deleteGenre() {
        // given
        var expectedGenres = List.of(Genre.of("Documentary"), Genre.of("Animation"));
        createTestData(expectedGenres);
        var genreToDelete = expectedGenres.getFirst();
        // when
        genreService.deleteGenre(genreToDelete.getId());
        // then
        assertThatThrownBy(() -> genreService.deleteGenre(genreToDelete.getId()))
                .isExactlyInstanceOf(GenreNotFound.class);
    }

    @Test
    void throwException_whenDeletingNotExistingGenre() {
        // given
        var expectedGenres = List.of(Genre.of("Documentary"), Genre.of("Animation"));
        var genreToDelete = expectedGenres.getLast();
        // when-then
        assertThatThrownBy(() -> genreService.deleteGenre(genreToDelete.getId()))
                .isExactlyInstanceOf(GenreNotFound.class);
    }

    @AfterEach
    void cleanup() {
        genreService.deleteAll();
    }

    private void createTestData(List<Genre> genres) {
        genreService.saveAll(genres);
    }
}
