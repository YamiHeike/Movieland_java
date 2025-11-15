package com.example.movieland.movie;

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

import java.util.List;
import java.util.UUID;

interface MovieApi {

    @Operation(summary = "Get paginated list of movies", description = "Returns a page of movies with details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movies retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "content": [
                                        {
                                          "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                                          "title": "Inception",
                                          "releaseDate": "2010-07-16",
                                          "genreId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                          "actors": [
                                            {"id": "1a2b3c4d-5678-90ab-cdef-1234567890ab","firstName": "Leonardo","lastName": "DiCaprio"},
                                            {"id": "2b3c4d5e-6789-01bc-def0-2345678901bc","firstName": "Joseph","lastName": "Gordon-Levitt"}
                                          ]
                                        }
                                      ],
                                      "pageable": {"pageNumber":0,"pageSize":10},
                                      "totalPages": 1,
                                      "totalElements": 1
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid page or size",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"status":"BAD_REQUEST","message":"Page number must be non-negative"}
                                    """)))
    })
    ResponseEntity<Page<Movie>> getMovies(
            @Parameter(description = "Page number, starting from 0") int page,
            @Parameter(description = "Number of movies per page") int size
    );

    @Operation(summary = "Get a movie by title", description = "Finds a movie by its title")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movie found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                                      "title": "Inception",
                                      "releaseDate": "2010-07-16",
                                      "genreId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                      "actors": [
                                        {"id": "1a2b3c4d-5678-90ab-cdef-1234567890ab","firstName": "Leonardo","lastName": "DiCaprio"},
                                        {"id": "2b3c4d5e-6789-01bc-def0-2345678901bc","firstName": "Joseph","lastName": "Gordon-Levitt"}
                                      ]
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Movie not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"status":"NOT_FOUND","message":"Movie with title 'Inception' not found"}
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"status":"BAD_REQUEST","message":"Title parameter cannot be empty"}
                                    """)))
    })
    ResponseEntity<Movie> getMovie(
            @Parameter(description = "Title of the movie") String title
    );

    @Operation(summary = "Get movies by actor ID", description = "Returns all movies that a specific actor acted in")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movies retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                                        "title": "Inception",
                                        "releaseDate": "2010-07-16",
                                        "genreId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        "actors": [
                                          {"id": "1a2b3c4d-5678-90ab-cdef-1234567890ab","firstName": "Leonardo","lastName": "DiCaprio"}
                                        ]
                                      }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid actor ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"status":"BAD_REQUEST","message":"Actor ID cannot be null"}
                                    """)))
    })
    ResponseEntity<List<Movie>> getMovieByActorId(
            @Parameter(description = "UUID of the actor") UUID id
    );

    @Operation(summary = "Get a movie by ID", description = "Finds a movie by its UUID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movie found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                                      "title": "Inception",
                                      "releaseDate": "2010-07-16",
                                      "genreId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                      "actors": [
                                        {"id": "1a2b3c4d-5678-90ab-cdef-1234567890ab","firstName": "Leonardo","lastName": "DiCaprio"},
                                        {"id": "2b3c4d5e-6789-01bc-def0-2345678901bc","firstName": "Joseph","lastName": "Gordon-Levitt"}
                                      ]
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Movie not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"status":"NOT_FOUND","message":"Movie with ID 'f47ac10b-58cc-4372-a567-0e02b2c3d479' not found"}
                                    """)))
    })
    ResponseEntity<Movie> getMovie(
            @Parameter(description = "UUID of the movie") UUID id
    );

    @Operation(summary = "Create a new movie", description = "Creates a new movie if it does not exist")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movie created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                                      "title": "Inception",
                                      "releaseDate": "2010-07-16",
                                      "genreId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                      "actors": [
                                        {"id": "1a2b3c4d-5678-90ab-cdef-1234567890ab","firstName": "Leonardo","lastName": "DiCaprio"},
                                        {"id": "2b3c4d5e-6789-01bc-def0-2345678901bc","firstName": "Joseph","lastName": "Gordon-Levitt"}
                                      ]
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Movie already exists or invalid request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"status":"BAD_REQUEST","message":"Movie with title 'Inception' already exists"}
                                    """)))
    })
    ResponseEntity<Movie> createMovie(
            @Parameter(description = "Movie creation request") CreateMovieRequest request
    );

    @Operation(summary = "Update a movie", description = "Updates movie details if it exists")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movie updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                                      "title": "Inception",
                                      "releaseDate": "2010-07-16",
                                      "genreId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                      "actors": [
                                        {"id": "1a2b3c4d-5678-90ab-cdef-1234567890ab","firstName": "Leonardo","lastName": "DiCaprio"},
                                        {"id": "2b3c4d5e-6789-01bc-def0-2345678901bc","firstName": "Joseph","lastName": "Gordon-Levitt"}
                                      ]
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Movie not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"status":"NOT_FOUND","message":"Movie with ID 'f47ac10b-58cc-4372-a567-0e02b2c3d479' not found"}
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"status":"BAD_REQUEST","message":"Invalid movie data provided"}
                                    """)))
    })
    ResponseEntity<Movie> updateMovie(
            @Parameter(description = "Movie object with updated details") Movie movie
    );

    @Operation(summary = "Delete a movie by ID", description = "Deletes a movie by its UUID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Movie deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Movie not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"status":"NOT_FOUND","message":"Movie with ID 'f47ac10b-58cc-4372-a567-0e02b2c3d479' not found"}
                                    """)))
    })
    ResponseEntity<Void> deleteMovie(
            @Parameter(description = "UUID of the movie to delete") UUID id
    );
}
