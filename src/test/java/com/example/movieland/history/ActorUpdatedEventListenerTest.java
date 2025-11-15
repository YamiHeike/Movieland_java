package com.example.movieland.history;

import com.example.movieland.actor.ActorService;
import com.example.movieland.common.BaseIntegrationTest;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.movieland.movie.MovieTestData.getTestActors;
import static com.example.movieland.history.HistoryTestDataSupplier.CHANGED_ACTOR_IDX;
import static com.example.movieland.history.HistoryTestDataSupplier.CHANGED_NAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ActorUpdatedEventListenerTest extends BaseIntegrationTest {
    @Autowired
    private ActorService actorService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private HistoryTestDataSupplier testDataSupplier;

    @Test
    void updateActorOnEvent() {
        // given
        testDataSupplier.prepareDataSet();

        var newName = "Carrie A.";
        var actorToUpdate = getTestActors().get(CHANGED_ACTOR_IDX);
        var expectedActorDetailEntries = List.of(
                ActorDetailsResponse.of(newName, actorToUpdate.getLastName()),
                ActorDetailsResponse.of(CHANGED_NAME, actorToUpdate.getLastName())
        );
        actorToUpdate.setFirstName(newName);
        // when
        actorService.update(actorToUpdate, true);
        var actorHistory = historyService.getActorDataChangeHistory(actorToUpdate.getId());
        // then
        assertThat(actorHistory).satisfies(history -> {
            assertThat(history.entries().size()).isEqualTo(2);
            assertThat(history.entries()).usingRecursiveComparison()
                    .ignoringCollectionOrder()
                    .isEqualTo(expectedActorDetailEntries);
        });
    }

    @AfterEach
    void cleanup() {
        actorService.deleteAll();
    }
}
