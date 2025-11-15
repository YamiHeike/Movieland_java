package com.example.movieland.history;

import com.example.movieland.actor.ActorService;
import com.example.movieland.movie.ActorSnapshot;
import com.example.movieland.movie.CreateMovieRequest;
import com.example.movieland.movie.Movie;
import com.example.movieland.movie.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final MovieService movieService;
    private final ActorService actorService;

    @Transactional(readOnly = true)
    public ActorHistoryResponse getActorDataChangeHistory(UUID actorId) {
        var movies = movieService.getByActorId(actorId);
        if(movies.isEmpty())
            return ActorHistoryResponse.of(actorId, emptyList());
        var entries = movies.stream()
                .flatMap(it -> it.getActors().stream()
                        .filter(snapshot -> snapshot.id().equals(actorId)))
                .distinct()
                .map(it -> ActorDetailsResponse.of(it.firstName(), it.lastName()))
                .toList();
        return ActorHistoryResponse.of(actorId, entries);
    }

    @Transactional(readOnly = true)
    public ActorDetailsResponse getActorNameInMovie(UUID movieId, UUID actorId) {
        var movie = movieService.getById(movieId);
        var actor = movie.getActors().stream()
                .filter(snapshot -> snapshot.id().equals(actorId))
                .findFirst()
                .orElseThrow(() -> new ActorNotCastInMovie("Actor id %s was not cast in movie %s".formatted(actorId, movie.getId())));
        return ActorDetailsResponse.of(actor.firstName(), actor.lastName());
    }

    @Transactional(readOnly = true)
    public boolean didActorChangedDataOverTime(UUID actorID) {
        var actor = actorService.findById(actorID);
        var currentName = "%s %s".formatted(actor.getFirstName(), actor.getLastName());
        var movies = movieService.getByActorId(actorID);
        if(movies.isEmpty())
            return false;
        return movies.stream()
                .flatMap(it -> it.getActors().stream()
                        .filter(snapshot -> snapshot.id().equals(actorID)))
                .anyMatch(snapshot -> !(("%s %s").formatted(snapshot.firstName(), snapshot.lastName()).equalsIgnoreCase(currentName.trim())));
    }

    @Transactional
    public void updateSnapshots(UUID actorId, String previousFirstName, String previousLastName, String newFirstName, String newLastName) {
        var movies = movieService.getByActorId(actorId).stream()
                        .filter(movie -> movie.getActors().stream()
                                .anyMatch(snapshot -> snapshot.firstName().equals(previousFirstName)
                                        && snapshot.lastName().equals(previousLastName)
                                        && snapshot.id().equals(actorId)))
                        .toList();
        movies.forEach(movie -> {
            var updatedActors = movie.getActors().stream()
                    .map(snapshot -> snapshot.id().equals(actorId)
                            ? ActorSnapshot.from(actorId, newFirstName, newLastName)
                            : snapshot)
                    .toList();
            var updatedMovie = Movie.fromRequest(movie.getId(), CreateMovieRequest.of(movie.getTitle(), movie.getReleaseDate(), movie.getGenreId(), updatedActors));
            movieService.update(updatedMovie);
        });
    }
}
