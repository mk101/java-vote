package org.example.server.model.request;

import lombok.Data;

import java.util.Map;

@Data
public class VoteRequest {

    private String topic;
    private String vote;
    private String choose;

    public VoteRequest(Map<String, Object> json) {
        if (!json.containsKey("topic")) {
            throw new IllegalArgumentException("Payload must contains 'topic' field");
        }
        this.topic = (String) json.get("topic");

        if (!json.containsKey("vote")) {
            throw new IllegalArgumentException("Payload must contains 'vote' field");
        }
        this.vote = (String) json.get("vote");

        if (!json.containsKey("choose")) {
            throw new IllegalArgumentException("Payload must contains 'choose' field");
        }
        this.choose = (String) json.get("choose");
    }

}
