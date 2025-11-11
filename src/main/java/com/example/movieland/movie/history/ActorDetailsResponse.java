package com.example.movieland.movie.history;

public record ActorDetailsResponse(String firstName, String lastName) {
    public static ActorDetailsResponse of(String firstName, String lastName) {
        return new ActorDetailsResponse(firstName, lastName);
    }
}
