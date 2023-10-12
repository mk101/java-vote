package org.example.server.service.message_processing;

import org.assertj.core.util.Lists;
import org.example.common.dto.HeaderDto;
import org.example.common.dto.MessageAction;
import org.example.common.dto.MessageDto;
import org.example.server.model.Topic;
import org.example.server.model.Vote;
import org.example.server.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DeleteVoteServiceTests {

    private Repository repository;

    private DeleteVoteService service;

    @BeforeEach
    public void init() {
        repository = mock(Repository.class);

        service = new DeleteVoteService(repository);
    }

    @Test
    public void correctDelete() {
        MessageDto message = new MessageDto(new HeaderDto("usr", MessageAction.DELETE), Map.of("topic", "t",
                "vote", "v"));
        Vote vote = new Vote("v", "d", Lists.newArrayList(), "usr");
        Topic topic = new Topic("t", Map.of("v", vote));
        when(repository.getTopicByName("t")).thenReturn(Optional.of(topic));

        service.delete(message);

        verify(repository).deleteVote("t", vote);
    }

    @Test
    public void nullTopic() {
        MessageDto message = new MessageDto(new HeaderDto("usr", MessageAction.DELETE), Map.of("topic", "t",
                "vote", "v"));
        when(repository.getTopicByName("t")).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> service.delete(message));
    }

    @Test
    public void wrongUser() {
        MessageDto message = new MessageDto(new HeaderDto("qwerty", MessageAction.DELETE), Map.of("topic", "t",
                "vote", "v"));
        Vote vote = new Vote("v", "d", Lists.newArrayList(), "usr");
        Topic topic = new Topic("t", Map.of("v", vote));
        when(repository.getTopicByName("t")).thenReturn(Optional.of(topic));

        assertThrows(IllegalStateException.class, () -> service.delete(message));
    }

}
