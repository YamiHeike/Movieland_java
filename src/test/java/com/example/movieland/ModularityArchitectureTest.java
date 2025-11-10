package com.example.movieland;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.test.ApplicationModuleTest;

@ApplicationModuleTest
public class ModularityArchitectureTest {
    @Test
    void verifyModules() {
        var modules = ApplicationModules.of(MovielandApplication.class);
        modules.verify();
    }
}
