package com.example.movieland.actor;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ActorService {
    private final ActorRepository actorRepository;

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

    public Actor createActor(@NonNull CreateActorRequest request) {
        if(actorRepository.findByFirstNameAndLastNameAndBirthDateIgnoreCase(request.firstName(), request.lastName(), request.birthDate()).isPresent()) {
            throw new ActorAlreadyExists("Actor %s %s, birth date: %s already exists".formatted(request.firstName(), request.lastName(), request.birthDate()));
        }
        var newActor = Actor.fromRequest(UUID.randomUUID(), request);
        return save(newActor);
    }

    public Actor update(@NonNull Actor actor) {
        var existing = actorRepository.findById(actor.getId()).orElseThrow(() -> new ActorNotFound(AUTHOR_ID_NOT_FOUND.formatted(actor.getId())));
        existing.setFirstName(actor.getFirstName());
        existing.setLastName(actor.getLastName());
        existing.setBirthDate(actor.getBirthDate());
        return save(actor);
    }

    public void delete(@NonNull UUID id) {
        if (!actorRepository.existsById(id))
            throw new ActorNotFound(String.format(AUTHOR_ID_NOT_FOUND.formatted(id)));

        actorRepository.deleteById(id);
    }
}
