package com.example.movieland.actor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ActorRepository extends CrudRepository<Actor, ActorId> {
    Page<Actor> findAll(Pageable pageable);
}
