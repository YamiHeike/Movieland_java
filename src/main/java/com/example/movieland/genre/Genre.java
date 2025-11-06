package com.example.movieland.genre;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "genres")
public class Genre {
    @Id
    private final UUID id;
    @NotBlank
    private String name;

    public static Genre of(String name) {
        return new Genre(UUID.randomUUID(), name);
    }

    public static Genre fromRequest(UUID id, CreateGenreRequest request) {
        return new Genre(id, request.name());
    }
}
