package org.example.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topic {

    private String name;

    private Map<String, Vote> votes;

    public Topic(String name) {
        this.name = name;
        this.votes = new HashMap<>();
    }

}
