package org.example.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vote {

    private String name;
    private String description;
    private Map<String, Set<String>> answers;
    private String creatorUsername;

    public Vote(String name, String description, List<String> answers, String creatorUsername) {
        this.name = name;
        this.description = description;
        this.creatorUsername = creatorUsername;
        this.answers = answers.stream().collect(Collectors.toMap(Function.identity(), (s) -> new HashSet<>()));
    }

}
