package com.example.movieland.history;

import com.example.movieland.common.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

interface HistoryApi {

    @Operation(
            summary = "Get actor name history across all movies",
            description = """
            Returns a list of all unique historical snapshots of the actor's name as they appear in movie casts.
            Snapshots reflect how the actor was named **at the time the movie was saved**, and may differ
            from the actor's current name.
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Actor history retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ActorHistoryResponse.class),
                    examples = @ExampleObject("""
                {
                  "id": "51fae4c4-c1b7-4a4c-b2a3-28b894e3e12b",
                  "entries": [
                    {
                      "firstName": "Johnny",
                      "lastName": "Depp"
                    },
                    {
                      "firstName": "John",
                      "lastName": "Depp"
                    }
                  ]
                }
                """)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Actor not found",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject("""
                {
                  "status": 404,
                  "message": "Actor with id 51fae4c4-c1b7-4a4c-b2a3-28b894e3e12b not found"
                }
                """)
            )
    )
    ResponseEntity<ActorHistoryResponse> getActorNameChangeHistory(
            @Parameter(
                    description = "ID of the actor whose snapshot history will be returned",
                    required = true
            ) UUID actorId
    );

    @Operation(
            summary = "Check if an actor ever appeared under different names",
            description = """
            Returns true if at least one snapshot of the actor's name stored in movies
            does not match their current name.
            Useful for detecting old spellings, early-career names, or corrected data.
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Name-change flag successfully evaluated",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "boolean"),
                    examples = {
                            @ExampleObject(name = "Changed", value = "true"),
                            @ExampleObject(name = "No Change", value = "false")
                    }
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Actor not found",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject("""
                {
                  "status": 404,
                  "message": "Actor with id 51fae4c4-c1b7-4a4c-b2a3-28b894e3e12b not found"
                }
                """)
            )
    )
    ResponseEntity<Boolean> didActorChangeName(
            @Parameter(description = "ID of the actor to check", required = true)
            UUID actorId
    );

    @Operation(
            summary = "Get the actor's name as recorded in a specific movie",
            description = """
            Fetches the name of the actor exactly as it appears in that movie's snapshot.
            This may differ from the actor's current name if they renamed themselves later.
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Snapshot name returned successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ActorDetailsResponse.class),
                    examples = @ExampleObject("""
                {
                  "firstName": "Johnny",
                  "lastName": "Depp"
                }
                """)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Actor was not cast in the movie",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject("""
                {
                  "status": 400,
                  "message": "Actor id 51fae4c4-c1b7-4a4c-b2a3-28b894e3e12b was not cast in movie 9275d2bd-fa8e-48c1-92f6-5a36be7f9962"
                }
                """)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Movie not found",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject("""
                {
                  "status": 404,
                  "message": "Movie with id 9275d2bd-fa8e-48c1-92f6-5a36be7f9962 not found"
                }
                """)
            )
    )
    ResponseEntity<ActorDetailsResponse> getActorsNameInMovie(
            @Parameter(description = "Movie ID", required = true) UUID movieId,
            @Parameter(description = "Actor ID", required = true) UUID actorId
    );
}
