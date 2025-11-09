package com.example.movieland.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ActorSnapshot(@NotNull UUID id, @NotBlank String firstName, @NotBlank String lastName) {
    public static ActorSnapshot from(UUID id, String firstName, String lastName) {
        return new ActorSnapshot(id, firstName, lastName);
    }
}
