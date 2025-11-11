package com.example.movieland.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
class MovieController {
    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<Page<Movie>> getMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(movieService.getMovies(PageRequest.of(page, size)));
    }

    @GetMapping(params = "title")
    public ResponseEntity<Movie> getMovie(@RequestParam String title) {
        return ResponseEntity.ok(movieService.getByTitle(title));
    }

    @GetMapping(params = "actor-id")
    public ResponseEntity<List<Movie>> getMovieByActorId(@RequestParam("actor-id") UUID id) {
        return ResponseEntity.ok(movieService.getByActorId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable UUID id) {
        return ResponseEntity.ok(movieService.getById(id));
    }


    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody CreateMovieRequest request) {
        return ResponseEntity
                .status(CREATED)
                .body(movieService.create(request));
    }

    @PatchMapping
    public ResponseEntity<Movie> updateMovie(@RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.update(movie));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable UUID id) {
        movieService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
