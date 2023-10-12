package org.example.server.repository;

import org.example.server.model.Topic;

import java.util.List;

public interface SaveLoadRepository {

    void update(List<Topic> topics);

    List<Topic> getTopics();

}
