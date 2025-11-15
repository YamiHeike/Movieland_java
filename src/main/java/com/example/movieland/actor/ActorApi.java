package com.example.movieland.actor;

import com.example.movieland.common.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

interface ActorApi {

    @Operation(summary = "Get paginated list of actors", description = "Returns a page of actors")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of actors retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = Page.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "content": [
                                                {
                                                  "id": "123e4567-e89b-12d3-a456-426614174000",
                                                  "firstName": "John",
                                                  "lastName": "Doe",
                                                  "birthDate": "1990-01-01"
                                                }
                                              ],
                                              "pageable": {
                                                "pageNumber": 0,
                                                "pageSize": 10
                                              },
                                              "totalElements": 1,
                                              "totalPages": 1
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "BAD_REQUEST",
                                              "message": "Page number must be non-negative"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Page<Actor>> getActors(
            @Parameter(description = "Page number, starting from 0") int page,
            @Parameter(description = "Number of actors per page") int size
    );

    @Operation(summary = "Create a new actor", description = "Creates a new actor if it does not exist")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Actor created successfully",
                    content = @Content(
                            schema = @Schema(implementation = Actor.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": "123e4567-e89b-12d3-a456-426614174000",
                                              "firstName": "Jane",
                                              "lastName": "Smith",
                                              "birthDate": "1985-05-12"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Actor already exists or invalid request",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "BAD_REQUEST",
                                              "message": "Actor Jane Smith, birth date: 1985-05-12 already exists"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Actor> createActor(
            @Parameter(description = "Actor creation request") CreateActorRequest actor
    );

    @Operation(summary = "Get an actor by ID", description = "Finds an actor by their unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actor found",
                    content = @Content(
                            schema = @Schema(implementation = Actor.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": "123e4567-e89b-12d3-a456-426614174000",
                                              "firstName": "John",
                                              "lastName": "Doe",
                                              "birthDate": "1990-01-01"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "BAD_REQUEST",
                                              "message": "Invalid UUID format"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Actor not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "NOT_FOUND",
                                              "message": "Actor with id 123e4567-e89b-12d3-a456-426614174000 not found"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Actor> findActorById(
            @Parameter(description = "ID of the actor to retrieve") UUID id
    );

    @Operation(summary = "Update an actor", description = "Updates actor details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actor updated successfully",
                    content = @Content(
                            schema = @Schema(implementation = Actor.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": "123e4567-e89b-12d3-a456-426614174000",
                                              "firstName": "John",
                                              "lastName": "Doe",
                                              "birthDate": "1990-01-01"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "BAD_REQUEST",
                                              "message": "Invalid update request"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Actor not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "NOT_FOUND",
                                              "message": "Actor with id 123e4567-e89b-12d3-a456-426614174000 not found"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Actor> updateActor(
            @Parameter(description = "Actor object with updated details") Actor actor,
            @Parameter(description = "Whether to update snapshots") boolean updateSnapshots
    );

    @Operation(summary = "Delete an actor by ID", description = "Deletes an actor given its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Actor deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "BAD_REQUEST",
                                              "message": "Invalid UUID format"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Actor not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "NOT_FOUND",
                                              "message": "Actor with id 123e4567-e89b-12d3-a456-426614174000 not found"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Void> deleteActorById(
            @Parameter(description = "ID of the actor to delete") UUID id
    );
}
