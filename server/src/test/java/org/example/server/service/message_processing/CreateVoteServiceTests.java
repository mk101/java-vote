package org.example.server.service.message_processing;

import org.assertj.core.util.Lists;
import org.example.server.model.Vote;
import org.example.server.model.request.CreateVoteRequest;
import org.example.server.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CreateVoteServiceTests {

    private Repository repository;

    private CreateVoteService service;

    @BeforeEach
    public void init() {
        repository = mock(Repository.class);

        service = new CreateVoteService(repository);
    }

    @Test
    public void create() {
        ArgumentCaptor<Vote> captor = ArgumentCaptor.forClass(Vote.class);
        CreateVoteRequest request = new CreateVoteRequest(Map.of(
                "topic", "t",
                "name", "n",
                "description", "d",
                "answers", Lists.newArrayList("1")
        ));
        Vote vote = new Vote("n", "d", Lists.newArrayList("1"), "usr");

        assertDoesNotThrow(() -> service.create(request, "usr"));

        verify(repository).addVoteToTopic(eq("t"), captor.capture());
        assertThat(captor.getValue()).isEqualTo(vote);
    }

}
