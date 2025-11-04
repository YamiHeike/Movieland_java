package com.example.movieland.actor;

import java.time.LocalDate;

public record CreateActorRequest(String firstName, String lastName, LocalDate birthDate) {
    public static CreateActorRequest of(String firstName, String lastName, LocalDate birthDate) {
        return new CreateActorRequest(firstName, lastName, birthDate);
    }
}
