package com.example.movieland.actor;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class ActorSnapshot {
    UUID id;
    @NotBlank
    String firstName;
    @NotBlank
    String lastName;
}
