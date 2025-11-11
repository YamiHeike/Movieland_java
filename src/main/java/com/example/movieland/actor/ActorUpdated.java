package com.example.movieland.actor;

import java.util.UUID;

public record ActorUpdated(UUID id, String previousFirstName, String previousLastName, String newFirstName, String newLastName) {
    public static ActorUpdated with(UUID id, String previousFirstName, String previousLastName, String newFirstName, String newLastName) {
        return new ActorUpdated(id, previousFirstName, previousLastName, newFirstName, newLastName);
    }
}
