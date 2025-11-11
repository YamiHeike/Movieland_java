package com.example.movieland.movie.history;

import com.example.movieland.actor.Actor;
import com.example.movieland.actor.ActorService;
import com.example.movieland.actor.CreateActorRequest;
import com.example.movieland.movie.ActorSnapshot;
import com.example.movieland.movie.MovieService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.example.movieland.movie.MovieTestData.getTestMovies;

@Component
@Profile("test")
public class HistoryTestDataSupplier {
    public static final int CHANGED_MOVIE_IDX = 4;
    public static final int CHANGED_ACTOR_IDX = 1;
    public static final String CHANGED_NAME = "Carriie";
    private final MovieService movieService;
    private final ActorService actorService;

    public HistoryTestDataSupplier(MovieService movieService, ActorService actorService) {
        this.movieService = movieService;
        this.actorService = actorService;
    }

    // TODO: investigate - movies should not be created when actor does not exist
    public UUID prepareDataSet() {
        var movies = getTestMovies();
        var actors = getTestMovies().stream()
                .flatMap(movie -> movie.getActors().stream())
                .map(snapshot -> Actor.fromRequest(snapshot.id(), CreateActorRequest.of(
                        snapshot.firstName(),
                        snapshot.lastName(),
                        LocalDate.of(1999, 1, 2)
                )))
                .toList();
        actors.forEach(actorService::save);
        movieService.saveAll(movies);
        var movie = movies.get(CHANGED_MOVIE_IDX);
        var snapshot = movie.getActors().get(CHANGED_ACTOR_IDX);
        var editedSnapshot = ActorSnapshot.from(snapshot.id(), CHANGED_NAME, snapshot.lastName());
        var newSnapshots = List.of(movie.getActors().getFirst(), editedSnapshot);
        movie.setActors(newSnapshots);
        movieService.update(movie);
        return movie.getId();
    }
}
