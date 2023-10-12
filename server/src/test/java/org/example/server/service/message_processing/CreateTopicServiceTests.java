package org.example.server.service.message_processing;

import org.example.common.dto.HeaderDto;
import org.example.common.dto.MessageAction;
import org.example.common.dto.MessageDto;
import org.example.server.model.Topic;
import org.example.server.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CreateTopicServiceTests {

    private Repository repository;

    private CreateTopicService service;

    @BeforeEach
    public void init() {
        repository = mock(Repository.class);

        service = new CreateTopicService(repository);
    }

    @Test
    public void createCorrectTopic() {
        ArgumentCaptor<Topic> captor = ArgumentCaptor.forClass(Topic.class);
        String name = "test";
        Topic topic = new Topic(name);
        MessageDto message = new MessageDto(new HeaderDto("usr", MessageAction.CREATE_TOPIC), Map.of("name", name));

        service.createTopic(message);

        verify(repository).addTopic(captor.capture());
        assertThat(captor.getValue()).isEqualTo(topic);
    }

    @Test
    public void withoutName() {
        MessageDto message = new MessageDto(new HeaderDto("usr", MessageAction.CREATE_TOPIC), new HashMap<>());

        assertThrows(IllegalArgumentException.class, () -> service.createTopic(message));
    }

}
