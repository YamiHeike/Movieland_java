package com.example.movieland.movie;

import com.example.movieland.common.AppException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class MovieAlreadyExists extends AppException {
    public MovieAlreadyExists(String message) {
        super(BAD_REQUEST, message);
    }
}
