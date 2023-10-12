package org.example.server.service.message_processing;

import lombok.RequiredArgsConstructor;
import org.example.server.model.request.CreateVoteRequest;
import org.example.server.model.Vote;
import org.example.server.repository.Repository;

@RequiredArgsConstructor
public class CreateVoteService {

    private final Repository repository;

    public void create(CreateVoteRequest request, String username) {
        Vote vote = new Vote(request.getName(), request.getDescription(), request.getAnswers(), username);
        repository.addVoteToTopic(request.getTopic(), vote);
    }

}
