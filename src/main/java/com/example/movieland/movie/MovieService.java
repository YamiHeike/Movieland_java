package com.example.movieland.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private static String NOT_FOUND_MESSAGE = "Movie with id %s not found";

    public Page<Movie> getMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Movie getById(UUID id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(NOT_FOUND_MESSAGE.formatted(id)));
    }

    public Movie getByTitle(String title) {
        return movieRepository.findByTitle(title)
                .orElseThrow(() -> new MovieNotFoundException(NOT_FOUND_MESSAGE.formatted(title)));
    }

}
