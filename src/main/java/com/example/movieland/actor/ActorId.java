package com.example.movieland.actor;

import com.example.movieland.common.ResourceId;

import java.util.UUID;

public record ActorId(UUID id) implements ResourceId {
    public static ActorId generated() {
        return new ActorId(UUID.randomUUID());
    }

    @Override
    public String getName() {
        return "actor";
    }
}
