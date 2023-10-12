package org.example.server.service.message_processing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.server.model.Topic;
import org.example.server.model.Vote;
import org.example.server.model.request.VoteRequest;
import org.example.server.repository.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class VoteService {

    private final Repository repository;

    public void vote(VoteRequest request, String username) {
        Optional<Topic> optional = repository.getTopicByName(request.getTopic());
        if (optional.isEmpty()) {
            throw new NullPointerException(String.format("Topic with name '%s' doesn't exist", request.getTopic()));
        }

        Vote vote = optional.get().getVotes().get(request.getVote());
        if (vote == null) {
            throw new NullPointerException(String.format("Vote %s not found", request.getVote()));
        }

        if (!vote.getAnswers().containsKey(request.getChoose())) {
            throw new NullPointerException("Selected answer not found");
        }

        // if user has already voted
        checkAndRemoveVoice(vote, username);
        vote.getAnswers().get(request.getChoose()).add(username);
    }

    private void checkAndRemoveVoice(Vote vote, String user) {
        for (Map.Entry<String, Set<String>> entry : vote.getAnswers().entrySet()) {
            if (entry.getValue().contains(user)) {
                log.info("Delete user's vote in {}. User={}", vote, user);
                entry.getValue().remove(user);
                return;
            }
        }
    }

}
