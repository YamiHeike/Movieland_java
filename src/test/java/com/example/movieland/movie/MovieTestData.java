package com.example.movieland.movie;

import com.example.movieland.actor.Actor;
import com.example.movieland.actor.CreateActorRequest;
import com.example.movieland.genre.CreateGenreRequest;
import com.example.movieland.genre.Genre;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class MovieTestData {
    private final static UUID ACTION_UUID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private final static UUID SCI_FI_UUID  = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final ActorSnapshot KEANU = ActorSnapshot.from(
            UUID.fromString("33333333-3333-3333-3333-333333333333"), "Keanu", "Reeves");
    private static final ActorSnapshot CARRIE = ActorSnapshot.from(
            UUID.fromString("44444444-4444-4444-4444-444444444444"), "Carrie-Anne", "Moss");
    private static final ActorSnapshot LEO = ActorSnapshot.from(
            UUID.fromString("55555555-5555-5555-5555-555555555555"), "Leonardo", "DiCaprio");
    private static final ActorSnapshot MATTHEW = ActorSnapshot.from(
            UUID.fromString("66666666-6666-6666-6666-666666666666"), "Matthew", "McConaughey");
    private static final ActorSnapshot ANNE = ActorSnapshot.from(
            UUID.fromString("77777777-7777-7777-7777-777777777777"), "Anne", "Hathaway");

    public static List<Movie> getTestMovies() {
        Movie m1 = Movie.of(
                "The Matrix",
                LocalDate.of(1999, 3, 31),
                SCI_FI_UUID,
                List.of(KEANU, CARRIE)
        );
        Movie m2 = Movie.of(
                "John Wick",
                LocalDate.of(2014, 10, 24),
                ACTION_UUID,
                List.of(KEANU)
        );
        Movie m3 = Movie.of(
                "Inception",
                LocalDate.of(2010, 7, 16),
                SCI_FI_UUID,
                List.of(LEO)
        );
        Movie m4 = Movie.of(
                "Interstellar",
                LocalDate.of(2014, 11, 7),
                SCI_FI_UUID,
                List.of(MATTHEW, ANNE)
        );
        Movie m5 = Movie.of(
                "The Matrix Reloaded",
                LocalDate.of(2003, 5, 15),
                SCI_FI_UUID,
                List.of(KEANU, CARRIE)
        );
        Movie m6 = Movie.of(
                "The Matrix Revolutions",
                LocalDate.of(2003, 11, 5),
                SCI_FI_UUID,
                List.of(KEANU, CARRIE)
        );
        return List.of(m1, m2, m3, m4, m5, m6);
    }

    public static List<Genre> getTestGenres() {
        return List.of(Genre.fromRequest(ACTION_UUID, CreateGenreRequest.of("Action")), Genre.fromRequest(SCI_FI_UUID, CreateGenreRequest.of("Sci-Fi")));
    }

    public static List<Actor> getTestActors() {
        return List.of(
                Actor.fromRequest(KEANU.id(), CreateActorRequest.of(
                        KEANU.firstName(),
                        KEANU.lastName(),
                        LocalDate.of(1964, 9, 2)
                )),
                Actor.fromRequest(CARRIE.id(), CreateActorRequest.of(
                        CARRIE.firstName(),
                        CARRIE.lastName(),
                        LocalDate.of(1967, 8, 21)
                )),
                Actor.fromRequest(LEO.id(), CreateActorRequest.of(
                        LEO.firstName(),
                        LEO.lastName(),
                        LocalDate.of(1974, 11, 11)
                )),
                Actor.fromRequest(MATTHEW.id(), CreateActorRequest.of(
                        MATTHEW.firstName(),
                        MATTHEW.lastName(),
                        LocalDate.of(1969, 11, 4)
                )),
                Actor.fromRequest(ANNE.id(), CreateActorRequest.of(
                        ANNE.firstName(),
                        ANNE.lastName(),
                        LocalDate.of(1982, 11, 12)
                ))
        );
    }
}
