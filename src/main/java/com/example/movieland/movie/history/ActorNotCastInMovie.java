package com.example.movieland.movie.history;

import com.example.movieland.common.AppException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ActorNotCastInMovie extends AppException {
    public ActorNotCastInMovie(String message) {
        super(BAD_REQUEST, message);
    }
}
