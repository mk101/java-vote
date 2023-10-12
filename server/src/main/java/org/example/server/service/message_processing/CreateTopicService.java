package org.example.server.service.message_processing;

import lombok.RequiredArgsConstructor;
import org.example.common.dto.MessageDto;
import org.example.server.model.Topic;
import org.example.server.repository.Repository;

import java.util.Map;

@RequiredArgsConstructor
public class CreateTopicService {

    private final Repository repository;

    public void createTopic(MessageDto message) {
        Map<String, Object> payload = message.getPayload();
        if (!payload.containsKey("name")) {
            throw new IllegalArgumentException("Payload must contains 'name' field");
        }

        String name = (String) payload.get("name");
        repository.addTopic(new Topic(name));
    }

}
