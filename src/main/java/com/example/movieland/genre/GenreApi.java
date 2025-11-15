package com.example.movieland.genre;

import com.example.movieland.common.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

interface GenreApi {

    @Operation(summary = "Get all genres", description = "Returns a list of all genres")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Genres retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Genre.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"status\":\"BAD_REQUEST\",\"message\":\"Invalid request parameters\"}")))
    })
    ResponseEntity<List<Genre>> findAll();

    @Operation(summary = "Get a genre by ID", description = "Finds a genre by its unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Genre found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Genre.class,
                                    example = "{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"name\":\"Action\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"status\":\"BAD_REQUEST\",\"message\":\"Invalid request parameters\"}"))),
            @ApiResponse(responseCode = "404", description = "Genre not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"status\":\"NOT_FOUND\",\"message\":\"Genre with ID 123e4567-e89b-12d3-a456-426614174000 not found\"}")))
    })
    ResponseEntity<Genre> findGenre(UUID id);

    @Operation(summary = "Create a new genre", description = "Creates a genre if it does not already exist")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Genre created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Genre.class,
                                    example = "{\"id\":\"123e4567-e89b-12d3-a456-426614174001\",\"name\":\"Comedy\"}"))),
            @ApiResponse(responseCode = "400", description = "Genre already exists or invalid request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"status\":\"BAD_REQUEST\",\"message\":\"Genre with name Comedy already exists\"}")))
    })
    ResponseEntity<Genre> createGenre(CreateGenreRequest request);

    @Operation(summary = "Update a genre", description = "Updates genre details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Genre updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Genre.class,
                                    example = "{\"id\":\"123e4567-e89b-12d3-a456-426614174002\",\"name\":\"Drama\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"status\":\"BAD_REQUEST\",\"message\":\"Invalid request parameters\"}"))),
            @ApiResponse(responseCode = "404", description = "Genre not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"status\":\"NOT_FOUND\",\"message\":\"Genre with ID 123e4567-e89b-12d3-a456-426614174002 not found\"}")))
    })
    ResponseEntity<Genre> updateGenre(Genre genre);

    @Operation(summary = "Delete a genre by ID", description = "Deletes a genre given its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Genre deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"status\":\"BAD_REQUEST\",\"message\":\"Invalid request parameters\"}"))),
            @ApiResponse(responseCode = "404", description = "Genre not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"status\":\"NOT_FOUND\",\"message\":\"Genre with ID 123e4567-e89b-12d3-a456-426614174003 not found\"}")))
    })
    ResponseEntity<Void> deleteGenre(UUID id);
}
