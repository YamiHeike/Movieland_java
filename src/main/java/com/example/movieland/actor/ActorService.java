package com.example.movieland.actor;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Actor createActor(@NonNull CreateActorRequest request) {
        if(actorRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndBirthDate(request.firstName(), request.lastName(),  request.birthDate())) {
            throw new ActorAlreadyExists("Actor %s %s, birth date: %s already exists".formatted(request.firstName(), request.lastName(), request.birthDate().toString()));
        }
        var newActor = Actor.fromRequest(UUID.randomUUID(), request);
        return save(newActor);
    }

    @Transactional
    public Actor update(@NonNull Actor actor) {
        if(!actorRepository.existsById(actor.getId()))
            throw new ActorNotFound(String.format(AUTHOR_ID_NOT_FOUND, actor.getId()));
        return save(actor);
    }

    @Transactional
    public void delete(@NonNull UUID id) {
        if (!actorRepository.existsById(id))
            throw new ActorNotFound(String.format(AUTHOR_ID_NOT_FOUND.formatted(id)));

        actorRepository.deleteById(id);
    }
}
