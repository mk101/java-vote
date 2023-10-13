package org.example.client.client;

import org.example.client.model.CreateVoteBody;
import org.example.common.dto.MessageDto;

public interface Client {

    MessageDto createTopic(String name);

    MessageDto view();
    MessageDto view(String topic);
    MessageDto view(String topic, String vote);

    MessageDto createVote(CreateVoteBody body);

    MessageDto vote(String topic, String vote, String choose);

    MessageDto delete(String topic, String vote);

    MessageDto getQuestions(String topic, String vote);

}
