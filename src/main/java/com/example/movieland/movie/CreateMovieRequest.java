package com.example.movieland.movie;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateMovieRequest(String title, LocalDate releaseDate, UUID genreId, List<ActorSnapshot> actors) {
    public static CreateMovieRequest of(String title, LocalDate releaseDate, UUID genreId, List<ActorSnapshot> actors) {
        return new CreateMovieRequest(title, releaseDate, genreId, actors);
    }
}
