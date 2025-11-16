package com.example.movieland.history;

import com.example.movieland.actor.ActorService;
import com.example.movieland.genre.GenreService;
import com.example.movieland.movie.ActorSnapshot;
import com.example.movieland.movie.MovieService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.example.movieland.movie.MovieTestData.*;

@Component
@Profile("test")
public class HistoryTestDataSupplier {
    public static final int CHANGED_MOVIE_IDX = 4;
    public static final int CHANGED_ACTOR_IDX = 1;
    public static final String CHANGED_NAME = "Carrie";
    private final MovieService movieService;
    private final ActorService actorService;
    private final GenreService genreService;

    public HistoryTestDataSupplier(MovieService movieService, ActorService actorService, GenreService genreService) {
        this.movieService = movieService;
        this.actorService = actorService;
        this.genreService = genreService;
    }

    public UUID prepareDataSet() {
        var movies = getTestMovies();
        var actors = getTestActors();
        var genres = getTestGenres();
        genreService.saveAll(genres);
        actorService.saveAll(actors);
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
