package com.example.movieland.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
class GenreController {
    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<List<Genre>> findAll() {
        return ResponseEntity.ok(genreService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> findByGenre(@PathVariable UUID id) {
        return ResponseEntity.ok(genreService.findGenreById(id));
    }

    @PostMapping
    public ResponseEntity<Genre> createGenre(CreateGenreRequest request) {
        var genreCreated = genreService.createGenre(request);
        return ResponseEntity
                .status(CREATED)
                .body(genreCreated);
    }

    @PatchMapping
    public ResponseEntity<Genre> updateGenre(Genre genre) {
        var genreUpdated = genreService.updateGenre(genre);
        return ResponseEntity.ok(genreUpdated);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteGenre(UUID id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}
