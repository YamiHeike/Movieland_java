package com.example.movieland.actor;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/actors")
@AllArgsConstructor
class ActorController implements ActorApi {
    private final ActorService actorService;

    @GetMapping
    public ResponseEntity<Page<Actor>> getActors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(actorService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Actor> createActor(@RequestBody CreateActorRequest actor) {
        var actorCreated = actorService.createActor(actor);
        return ResponseEntity
                .status(CREATED)
                .body(actorCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actor> findActorById(@PathVariable UUID id) {
        var actor = actorService.findById(id);
        return ResponseEntity.ok(actor);
    }

    @PatchMapping
    public ResponseEntity<Actor> updateActor(@RequestBody Actor actor, @RequestParam(name = "update-snapshots", defaultValue = "false") boolean updateSnapshots) {
        var actorUpdated = actorService.update(actor, updateSnapshots);
        return ResponseEntity.ok(actorUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActorById(@PathVariable UUID id) {
        actorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
