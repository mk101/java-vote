package org.example.server.service.message_processing;

import org.assertj.core.util.Lists;
import org.example.server.model.Topic;
import org.example.server.model.Vote;
import org.example.server.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetInfoServiceTests {

    private Repository repository;

    private GetInfoService service;

    @BeforeEach
    public void init() {
        repository = mock(Repository.class);

        service = new GetInfoService(repository);
    }

    @Test
    public void getTopicsInfo() {
        String expect = "t (votes in topic=3)";
        Vote vote = new Vote("v", "d", Map.of("1", Set.of("a", "b"), "2", Set.of("c"), "3", Set.of()), "usr");
        Topic topic = new Topic("t", Map.of("v", vote));
        when(repository.getAllTopics()).thenReturn(Lists.newArrayList(topic));

        var result = service.getTopicsInfo();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(expect);
    }

    @Test
    public void getTopicVotes() {
        String expect = "v (votes=3)";
        Vote vote = new Vote("v", "d", Map.of("1", Set.of("a", "b"), "2", Set.of("c"), "3", Set.of()), "usr");
        Topic topic = new Topic("t", Map.of("v", vote));
        when(repository.getTopicByName("t")).thenReturn(Optional.of(topic));

        var result = service.getTopicVotes("t");

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(expect);
    }

    @Test
    public void getVoteInfo() {
        Vote vote = new Vote("v", "d", Map.of("1", Set.of("a", "b"), "2", Set.of("c"), "3", Set.of()), "usr");
        Topic topic = new Topic("t", Map.of("v", vote));
        when(repository.getTopicByName("t")).thenReturn(Optional.of(topic));

        var result = service.getVoteInfo("t", "v");

        assertThat(result).hasSize(4);
        assertThat(result.get(0)).isEqualTo("d");
    }

}
