package com.example.movieland.movie.history;

import java.util.List;
import java.util.UUID;

public record ActorHistoryResponse(UUID id, List<ActorDetailsResponse> entries) {
    public static ActorHistoryResponse of(UUID id, List<ActorDetailsResponse> entries) {
        return new ActorHistoryResponse(id, entries);
    }
}
