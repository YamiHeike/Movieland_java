package com.example.movieland.actor;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActorService {
    private final ActorRepository actorRepository;

    public Page<Actor> findAll(Pageable pageable) {
        return actorRepository.findAll(pageable);
    }
}
