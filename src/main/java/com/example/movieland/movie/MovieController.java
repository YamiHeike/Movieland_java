package com.example.movieland.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<Page<Movie>> getMovies(Pageable pageable) {
        return ResponseEntity.ok(movieService.getMovies(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable UUID id) {
        return ResponseEntity.ok(movieService.getById(id));
    }

}
