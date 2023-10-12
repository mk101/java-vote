package org.example.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.server.model.Topic;
import org.example.server.model.Vote;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class FileRepository implements Repository, SaveLoadRepository {

    private Map<String, Topic> topics = new HashMap<>();

    @Override
    public void addTopic(Topic topic) {
        if (topics.containsKey(topic.getName())) {
            throw new IllegalArgumentException("Name must be unique");
        }

        topics.put(topic.getName(), topic);
        log.info("Add topic {}", topic);
    }

    @Override
    public void addVoteToTopic(String topic, Vote vote) {
        if (!topics.containsKey(topic)) {
            throw new NullPointerException(String.format("Topic '%s' not found", topic));
        }

        var votes = topics.get(topic).getVotes();
        if (votes.containsKey(vote.getName())) {
            throw new IllegalArgumentException("Name must be unique");
        }
        votes.put(vote.getName(), vote);

        log.info("Add vote to topic {}", topics.get(topic));
    }

    @Override
    public List<Topic> getAllTopics() {
        return topics.values().stream().toList();
    }

    @Override
    public Optional<Topic> getTopicByName(String name) {
        return Optional.ofNullable(topics.get(name));
    }

    @Override
    public void deleteVote(String topic, Vote vote) {
        Optional<Topic> optional = getTopicByName(topic);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException(String.format("Topic %s not found", topic));
        }
        optional.get().getVotes().remove(vote.getName());

        log.info("Delete vote {} from topic {}", vote, optional.get());
    }

    @Override
    public void update(List<Topic> topics) {
        this.topics = topics.stream()
                .collect(Collectors.toMap(Topic::getName, Function.identity()));

        log.info("Set database to {}", topics);
    }

    @Override
    public List<Topic> getTopics() {
        return getAllTopics();
    }
}
