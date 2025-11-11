package com.example.movieland.genre;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final String GENRE_NOT_FOUND = "Genre with ID %s not found";

    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    public Genre findByName(@NotNull String name) {
        return genreRepository.findByName(name)
                .orElseThrow(() -> new GenreNotFound(GENRE_NOT_FOUND.formatted(name)));
    }

    public Genre findGenreById(@NotNull UUID id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFound(GENRE_NOT_FOUND.formatted(id)));
    }

    @Transactional
    public Genre updateGenre(@NotNull Genre genre) {
        if(!genreRepository.existsById(genre.getId()))
            throw new GenreNotFound(GENRE_NOT_FOUND.formatted(genre.getId()));
        return genreRepository.save(genre);
    }

    @Transactional
    public Genre createGenre(@NotNull CreateGenreRequest request) {
        if(genreRepository.existsByName(request.name()))
            throw new GenreAlreadyExists("Genre with name %s already exists".formatted(request.name()));
        var newGenre = Genre.fromRequest(UUID.randomUUID(), request);
        return genreRepository.save(newGenre);
    }

    @Transactional
    public void deleteGenre(@NotNull UUID id) {
        if(!genreRepository.existsById(id))
            throw new GenreNotFound(GENRE_NOT_FOUND.formatted(id));
        genreRepository.deleteById(id);
    }

    @Transactional
    public void saveAll(@NotNull List<Genre> genres) {
        genreRepository.saveAll(genres);
    }

    @Transactional
    public void deleteAll() {
        genreRepository.deleteAll();
    }
}
