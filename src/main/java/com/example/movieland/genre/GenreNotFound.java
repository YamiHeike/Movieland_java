package com.example.movieland.genre;

import com.example.movieland.common.AppException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class GenreNotFound extends AppException {
    public GenreNotFound(String message) {
        super(NOT_FOUND, message);
    }
}
