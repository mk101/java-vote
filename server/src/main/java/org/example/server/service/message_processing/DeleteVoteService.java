package org.example.server.service.message_processing;

import lombok.RequiredArgsConstructor;
import org.example.common.dto.MessageDto;
import org.example.server.model.Topic;
import org.example.server.model.Vote;
import org.example.server.repository.Repository;

import java.util.Optional;

@RequiredArgsConstructor
public class DeleteVoteService {

    private final Repository repository;

    public void delete(MessageDto message) {
        if (!message.getPayload().containsKey("topic")) {
            throw new IllegalArgumentException("Payload must contains 'topic' field");
        }
        if (!message.getPayload().containsKey("vote")) {
            throw new IllegalArgumentException("Payload must contains 'vote' field");
        }

        String topicName = (String) message.getPayload().get("topic");
        String voteName = (String) message.getPayload().get("vote");
        Optional<Topic> topic = repository.getTopicByName(topicName);
        if (topic.isEmpty()) {
            throw new NullPointerException(String.format("Topic with name '%s' doesn't exist", topicName));
        }

        if (!topic.get().getVotes().containsKey(voteName)) {
            throw new NullPointerException(String.format("Vote %s not found", voteName));
        }

        Vote vote = topic.get().getVotes().get(voteName);
        if (!vote.getCreatorUsername().equals(message.getHeader().getUser())) {
            throw new IllegalStateException("Only the user who created vote can delete it");
        }

        repository.deleteVote(topicName, vote);
    }

}
