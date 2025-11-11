package com.example.movieland.movie.history;

import com.example.movieland.actor.ActorUpdated;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActorUpdatedEventListener {
    private final HistoryService historyService;

    @EventListener
    public void updateSnapshots(ActorUpdated actorUpdated) {
        historyService.updateSnapshots(actorUpdated.id(), actorUpdated.previousFirstName(), actorUpdated.previousLastName(), actorUpdated.newFirstName(), actorUpdated.newLastName());
    }
}
