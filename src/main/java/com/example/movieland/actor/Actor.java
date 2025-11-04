package com.example.movieland.actor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "actors")
public class Actor {
    @Id @NotNull
    private final UUID id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private LocalDate birthDate;

    public static Actor from(String firstName, String lastName, LocalDate birthDate) {
        return new Actor(UUID.randomUUID(), firstName, lastName, birthDate);
    }

    public static Actor fromRequest(UUID id, CreateActorRequest request) {
        return new Actor(id, request.firstName(), request.lastName(),  request.birthDate());
    }
}