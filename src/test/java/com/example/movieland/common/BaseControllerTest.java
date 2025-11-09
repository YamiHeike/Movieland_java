package com.example.movieland.common;

import com.example.movieland.actor.ActorService;
import com.example.movieland.genre.GenreService;
import com.example.movieland.movie.MovieService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class BaseControllerTest extends BaseIntegrationTest {
    @LocalServerPort
    private int port;
    @Autowired
    public ActorService actorService;
    @Autowired
    public GenreService genreService;
    @Autowired
    public MovieService movieService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }
}
