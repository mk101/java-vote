package org.example.server.service.message_processing;

import lombok.RequiredArgsConstructor;
import org.example.server.model.Topic;
import org.example.server.model.Vote;
import org.example.server.repository.Repository;

import java.util.*;

@RequiredArgsConstructor
public class GetInfoService {

    private final Repository repository;

    public List<String> getTopicsInfo() {
        return repository.getAllTopics().stream()
                .map(this::formatTopic)
                .toList();
    }

    public List<String> getTopicVotes(String name) {
        Optional<Topic> topic = repository.getTopicByName(name);
        if (topic.isEmpty()) {
            throw new NullPointerException(String.format("Topic with name '%s' doesn't exist", name));
        }

        return topic.stream().map(t -> t.getVotes().values())
                .flatMap(Collection::stream)
                .map(this::formatVote)
                .toList();
    }

    public List<String> getVoteInfo(String topic, String name) {
        Optional<Topic> optional = repository.getTopicByName(topic);
        if (optional.isEmpty()) {
            throw new NullPointerException(String.format("Topic with name '%s' doesn't exist", topic));
        }

        Vote vote = optional.get().getVotes().get(name);
        if (vote == null) {
            throw new NullPointerException(String.format("Vote %s not found", name));
        }

        List<String> result = new ArrayList<>();
        result.add(vote.getDescription());
        result.addAll(vote.getAnswers().entrySet().stream().map(this::formatEntry).toList());

        return result;
    }

    private String formatTopic(Topic topic) {
        int count = topic.getVotes().values().stream().reduce(0, this::voteAccumulator, Integer::sum);
        return String.format("%s (votes in topic=%s", topic.getName(), count);
    }

    private Integer voteAccumulator(Integer result, Vote vote) {
        int votes = vote.getAnswers().values().stream()
                .mapToInt(Set::size)
                .sum();

        return result + votes;
    }

    private String formatVote(Vote vote) {
        int count = vote.getAnswers().values().stream().reduce(0, (sum, set) -> sum + set.size(), Integer::sum);

        return String.format("%s (votes=%s)", vote.getName(), count);
    }

    private String formatEntry(Map.Entry<String, Set<String>> entry) {
        return String.format("%s (%s)", entry.getKey(), entry.getValue().size());
    }

}
