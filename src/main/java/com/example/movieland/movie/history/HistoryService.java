package com.example.movieland.movie.history;

import com.example.movieland.actor.ActorService;
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
    public ActorHistoryResponse getActorHistory(UUID actorId) {
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
    public boolean didActorChangedData(UUID actorID) {
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
}
