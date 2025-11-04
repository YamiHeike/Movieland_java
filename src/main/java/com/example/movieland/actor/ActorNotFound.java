package com.example.movieland.actor;

import com.example.movieland.common.AppException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class ActorNotFound extends AppException {
    public ActorNotFound(String message) {
        super(NOT_FOUND, message);
    }
}
