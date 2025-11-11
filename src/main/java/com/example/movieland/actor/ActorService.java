package com.example.movieland.actor;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ActorService {
    private final ActorRepository actorRepository;
    private final ApplicationEventPublisher publisher;

    private static final String AUTHOR_ID_NOT_FOUND = "Actor with id %s not found";

    public Page<Actor> findAll(Pageable pageable) {
        return actorRepository.findAll(pageable);
    }

    public Actor findByFirstNameAndLastName(@NonNull String firstName, @NonNull String lastName) {
        return actorRepository.findByFirstNameAndLastNameIgnoreCase(firstName, lastName)
                .orElseThrow(() -> new ActorNotFound("%s %s - Actor not found".formatted(firstName, lastName)));
    }

    public Actor findById(@NonNull UUID id) {
        return actorRepository.findById(id).orElseThrow(() -> new ActorNotFound(AUTHOR_ID_NOT_FOUND.formatted(id)));
    }

    public Actor save(@NonNull Actor actor) {
        return actorRepository.save(actor);
    }

    @Transactional
    public Actor createActor(@NonNull CreateActorRequest request) {
        if(actorRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndBirthDate(request.firstName(), request.lastName(),  request.birthDate())) {
            throw new ActorAlreadyExists("Actor %s %s, birth date: %s already exists".formatted(request.firstName(), request.lastName(), request.birthDate().toString()));
        }
        var newActor = Actor.fromRequest(UUID.randomUUID(), request);
        return save(newActor);
    }

    @Transactional
    public Actor update(@NonNull Actor actor, boolean updateSnapshots) {
        var existing = actorRepository.findById(actor.getId()).orElseThrow(() -> new ActorNotFound(AUTHOR_ID_NOT_FOUND.formatted(actor.getId())));
        var previousFirstName = existing.getFirstName();
        var previousLastName = existing.getLastName();
        existing.setFirstName(actor.getFirstName());
        existing.setLastName(actor.getLastName());
        existing.setBirthDate(actor.getBirthDate());
        var updatedActor = save(actor);
        if(updateSnapshots) {
            var actorUpdatedEvent = ActorUpdated.with(existing.getId(), previousFirstName, previousLastName, actor.getFirstName(), actor.getLastName());
            publisher.publishEvent(actorUpdatedEvent);
        }
        return updatedActor;
    }

    @Transactional
    public void delete(@NonNull UUID id) {
        if (!actorRepository.existsById(id))
            throw new ActorNotFound(String.format(AUTHOR_ID_NOT_FOUND.formatted(id)));
        actorRepository.deleteById(id);
    }

    public boolean existsById(@NonNull UUID id) {
        return actorRepository.existsById(id);
    }

    @Transactional
    public void saveAll(@NonNull Iterable<Actor> actors) {
        actorRepository.saveAll(actors);
    }

    @Transactional
    public void deleteAll(List<Actor> actors) {
        actorRepository.deleteAll(actors);
    }

    @Transactional
    public void deleteAll() {
        actorRepository.deleteAll();
    }
}
