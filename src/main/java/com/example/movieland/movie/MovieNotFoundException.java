package com.example.movieland.movie;

import com.example.movieland.common.AppException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class MovieNotFoundException extends AppException {
    public MovieNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
