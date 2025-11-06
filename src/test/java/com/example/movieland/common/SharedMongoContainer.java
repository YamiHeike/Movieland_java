package com.example.movieland.common;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public class SharedMongoContainer {
    public static final MongoDBContainer INSTANCE = new MongoDBContainer(DockerImageName.parse("mongo:4.4.18"))
            .withReuse(true);
    static {
        INSTANCE.start();
    }

    private SharedMongoContainer() {}
}
