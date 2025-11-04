package com.example.movieland.common;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus status, String message) {}
