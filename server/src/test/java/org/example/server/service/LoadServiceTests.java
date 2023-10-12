package org.example.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.example.server.model.Topic;
import org.example.server.model.Vote;
import org.example.server.repository.SaveLoadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked", "rawtypes", "DataFlowIssue"})
public class LoadServiceTests {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private SaveLoadRepository repository;

    private LoadService service;

    @BeforeEach
    public void init() {
        repository = mock(SaveLoadRepository.class);

        service = new LoadService(repository, OBJECT_MAPPER);
    }

    @Test
    public void loadEmpty() {
        ArgumentCaptor<List<Topic>> captor = ArgumentCaptor.forClass((Class) List.class);
        String file = "empty-file.json";
        String path = Thread.currentThread().getContextClassLoader().getResource(file).getPath().substring(1);
        service.load(path);

        verify(repository).update(captor.capture());
        assertThat(captor.getValue()).isEmpty();
    }

    @Test
    public void loadContent() {
        ArgumentCaptor<List<Topic>> captor = ArgumentCaptor.forClass((Class) List.class);
        String file = "content.json";
        String path = Thread.currentThread().getContextClassLoader().getResource(file).getPath().substring(1);
        List<Topic> topics = Lists.newArrayList(new Topic("a", Map.of(
                "b", new Vote("b", "desc", Map.of("1", Set.of("usr")), "user")
        )));

        service.load(path);

        verify(repository).update(captor.capture());
        assertThat(captor.getValue()).isEqualTo(topics);
    }



}
