package com.example.movieland.actor;

import com.example.movieland.common.AppException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ActorAlreadyExists extends AppException {
    public ActorAlreadyExists(String message) {
        super(BAD_REQUEST, message);
    }
}
