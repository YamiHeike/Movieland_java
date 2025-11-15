package com.example.movieland.history;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/snapshot-history")
class HistoryController implements HistoryApi {
    private final HistoryService historyService;

    @GetMapping("/{actorId}")
    public ResponseEntity<ActorHistoryResponse> getActorNameChangeHistory(@PathVariable UUID actorId) {
        return ResponseEntity.ok(historyService.getActorDataChangeHistory(actorId));
    }

    @GetMapping("/{actorId}/name-change")
    public ResponseEntity<Boolean> didActorChangeName(@PathVariable UUID actorId) {
        return ResponseEntity.ok(historyService.didActorChangedDataOverTime(actorId));
    }

    @GetMapping("/movies/{movieId}/actors/{actorId}")
    public ResponseEntity<ActorDetailsResponse> getActorsNameInMovie(@PathVariable UUID movieId, @PathVariable UUID actorId) {
        return ResponseEntity.ok(historyService.getActorNameInMovie(movieId, actorId));
    }

}
