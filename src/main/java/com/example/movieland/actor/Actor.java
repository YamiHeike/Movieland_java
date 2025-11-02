package com.example.movieland.actor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "actors")
public record Actor(@Id ActorId id, String firstName, String lastName) {}