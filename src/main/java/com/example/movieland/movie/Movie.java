package com.example.movieland.movie;

import com.example.movieland.actor.ActorSnapshot;
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
    private String title;
    private LocalDate releaseDate;
    private UUID genreId;
    private List<ActorSnapshot> actors;

    public static Movie of(String title, LocalDate releaseDate, UUID genreId, List<ActorSnapshot> actors) {
        return new Movie(UUID.randomUUID(), title, releaseDate, genreId, actors);
    }
}
