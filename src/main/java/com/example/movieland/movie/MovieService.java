package com.example.movieland.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private static final String NOT_FOUND_MESSAGE = "Movie with id %s not found";

    public Page<Movie> getMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Movie getById(UUID id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFound(NOT_FOUND_MESSAGE.formatted(id)));
    }

    public Movie getByTitle(String title) {
        return movieRepository.findByTitle(title)
                .orElseThrow(() -> new MovieNotFound(NOT_FOUND_MESSAGE.formatted(title)));
    }

    public List<Movie> getByActorId(UUID actorId) {
        return movieRepository.findByActorId(actorId);
    }

    @Transactional
    public Movie create(CreateMovieRequest request) {
        if(movieRepository.existsByTitleAndReleaseDate(request.title(), request.releaseDate()))
            throw new MovieAlreadyExists("Movie with title %s already exists".formatted(request.title()));
        var createdMovie = Movie.fromRequest(UUID.randomUUID(), request);
        return movieRepository.save(createdMovie);
    }

    @Transactional
    public Movie update(Movie movie) {
        if(!movieRepository.existsById(movie.getId()))
            throw new MovieNotFound(String.format(NOT_FOUND_MESSAGE.formatted(movie.getId())));
        return movieRepository.save(movie);
    }

    @Transactional
    public void delete(UUID id) {
        if(!movieRepository.existsById(id))
            throw new MovieNotFound(String.format(NOT_FOUND_MESSAGE.formatted(id)));
        movieRepository.deleteById(id);
    }

    @Transactional
    public void saveAll(List<Movie> movies) {
        var saved = movieRepository.saveAll(movies);
    }

    @Transactional
    public void deleteAll() {
        movieRepository.deleteAll();
    }
}
