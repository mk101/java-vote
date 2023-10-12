package org.example.server.repository;

import org.example.server.model.Topic;
import org.example.server.model.Vote;

import java.util.List;
import java.util.Optional;

public interface Repository {

    void addTopic(Topic topic);
    void addVoteToTopic(String topic, Vote vote);

    List<Topic> getAllTopics();
    Optional<Topic> getTopicByName(String name);

    void deleteVote(String topic, Vote vote);

}
