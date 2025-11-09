package com.example.movieland.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "movies")
public class Movie {
    @Id
    private final UUID id;
    @NotBlank
    private String title;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    private UUID genreId;
    @NotNull
    private List<ActorSnapshot> actors;

    public static Movie of(String title, LocalDate releaseDate, UUID genreId, List<ActorSnapshot> actors) {
        return new Movie(UUID.randomUUID(), title, releaseDate, genreId, actors);
    }

    public static Movie fromRequest(UUID id, CreateMovieRequest request) {
        return new Movie(id, request.title(), request.releaseDate(), request.genreId(), request.actors());
    }
}
