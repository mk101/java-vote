package org.example.server.service.message_processing;

import org.example.server.model.Topic;
import org.example.server.model.Vote;
import org.example.server.model.request.VoteRequest;
import org.example.server.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VoteServiceTests {

    private Repository repository;

    private VoteService service;

    @BeforeEach
    public void init() {
        repository = mock(Repository.class);

        service = new VoteService(repository);
    }

    @Test
    public void vote() {
        Vote vote = new Vote("v", "d", Map.of("1", Set.of("a", "b"), "2", new HashSet<>()), "usr");
        Topic topic = new Topic("t", Map.of("v", vote));
        when(repository.getTopicByName("t")).thenReturn(Optional.of(topic));

        service.vote(new VoteRequest(Map.of("topic", "t", "vote", "v", "choose", "2")), "u");

        assertThat(vote.getAnswers().get("2")).hasSize(1);
    }

    @Test
    public void reVote() {
        Vote vote = new Vote("v", "d", Map.of("1", new HashSet<>(Set.of("a", "b", "u")), "2", new HashSet<>()), "usr");
        Topic topic = new Topic("t", Map.of("v", vote));
        when(repository.getTopicByName("t")).thenReturn(Optional.of(topic));

        service.vote(new VoteRequest(Map.of("topic", "t", "vote", "v", "choose", "2")), "u");

        assertThat(vote.getAnswers().get("1")).hasSize(2);
        assertThat(vote.getAnswers().get("2")).hasSize(1);
    }

}
