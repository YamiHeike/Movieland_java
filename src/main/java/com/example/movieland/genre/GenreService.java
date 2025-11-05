package com.example.movieland.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Genre findByName(String name) {
        return genreRepository.findByName(name)
                .orElseThrow(() -> new GenreNotFound(GENRE_NOT_FOUND.formatted(name)));
    }

    public Genre updateGenre(Genre genre) {
        if(!genreRepository.existsById(genre.getId()))
            throw new GenreNotFound(GENRE_NOT_FOUND.formatted(genre.getId()));
        return genreRepository.save(genre);
    }

    public Genre findGenreById(UUID id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFound(GENRE_NOT_FOUND.formatted(id)));
    }

    public Genre createGenre(CreateGenreRequest request) {
        if(genreRepository.existsByName(request.name()))
            throw new GenreAlreadyExists("Genre with name %s already exists".formatted(request.name()));
        var newGenre = Genre.fromRequest(UUID.randomUUID(), request);
        return genreRepository.save(newGenre);
    }

    public void deleteGenre(UUID id) {
        if(!genreRepository.existsById(id))
            throw new GenreNotFound(GENRE_NOT_FOUND.formatted(id));
        genreRepository.deleteById(id);
    }
}
