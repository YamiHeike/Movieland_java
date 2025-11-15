package com.example.movieland.common;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

public class SharedMongoContainer {
    public static final MongoDBContainer INSTANCE = new MongoDBContainer(DockerImageName.parse("mongo:4.4.18"))
            .withCommand("--replSet rs0 --bind_ip_all")
            .withReuse(true);
    static {
        INSTANCE.start();
//        try {
//            INSTANCE.execInContainer("mongosh", "--eval",
//                    "rs.initiate({_id: 'rs0', members: [{ _id: 0, host: '"
//                            + INSTANCE.getHost() + ":" + INSTANCE.getFirstMappedPort() + "' }]})");
//        } catch (IOException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

    private SharedMongoContainer() {}
}
