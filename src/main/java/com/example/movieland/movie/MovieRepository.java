package com.example.movieland.movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieRepository extends CrudRepository<Movie, UUID> {
    Page<Movie> findAll(Pageable pageable);
    Optional<Movie> findByTitle(String title);
    @Query("{ 'actors.id': ?0 }")
    List<Movie> findByActorId(UUID actorId);
}
