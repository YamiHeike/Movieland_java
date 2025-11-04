package com.example.movieland.actor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
interface ActorRepository extends CrudRepository<Actor, UUID> {
    Page<Actor> findAll(Pageable pageable);
    Optional<Actor> findByFirstNameAndLastNameIgnoreCase(String firstName, String lastName);
    Optional<Actor> findByFirstNameAndLastNameAndBirthDateIgnoreCase(String firstName, String lastName, LocalDate birthday);
}
