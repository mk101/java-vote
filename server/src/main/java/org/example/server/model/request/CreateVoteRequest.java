package org.example.server.model.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CreateVoteRequest {

    private String topic;
    private String name;
    private String description;
    private List<String> answers;

    @SuppressWarnings("unchecked")
    public CreateVoteRequest(Map<String, Object> json) {
        if (!json.containsKey("topic")) {
            throw new IllegalArgumentException("Payload must contains 'topic' field");
        }
        this.topic = (String) json.get("topic");

        if (!json.containsKey("name")) {
            throw new IllegalArgumentException("Payload must contains 'name' field");
        }
        this.name = (String) json.get("name");

        if (!json.containsKey("description")) {
            throw new IllegalArgumentException("Payload must contains 'description' field");
        }
        this.description = (String) json.get("description");

        if (!json.containsKey("answers")) {
            throw new IllegalArgumentException("Payload must contains 'answers' field");
        }
        this.answers = (List<String>) json.get("answers");
    }

}
