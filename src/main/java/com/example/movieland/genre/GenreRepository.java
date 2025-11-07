package com.example.movieland.genre;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface GenreRepository extends ListCrudRepository<Genre, UUID> {
    Optional<Genre> findByName(String name);
    boolean existsByName(String name);
}
