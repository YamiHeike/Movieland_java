package com.example.movieland.actor;

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
public class Actor{
    @Id
    private final UUID id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    public static Actor from(String firstName, String lastName, LocalDate birthDate) {
        return new Actor(UUID.randomUUID(), firstName, lastName, birthDate);
    }
}