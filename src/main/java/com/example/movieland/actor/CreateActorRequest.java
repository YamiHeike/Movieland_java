package com.example.movieland.actor;

import java.time.LocalDate;

public record CreateActorRequest(String firstName, String lastName, LocalDate birthDate) {
}
