package com.example.movieland.genre;

public record CreateGenreRequest(String name) {
    public static CreateGenreRequest of(String name) {
        return new CreateGenreRequest(name);
    }
}
