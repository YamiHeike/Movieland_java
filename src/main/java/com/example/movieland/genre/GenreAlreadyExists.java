package com.example.movieland.genre;

import com.example.movieland.common.AppException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class GenreAlreadyExists extends AppException {
    public GenreAlreadyExists(String message) {
        super(BAD_REQUEST ,message);
    }
}
