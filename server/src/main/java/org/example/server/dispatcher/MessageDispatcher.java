package org.example.server.dispatcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.dto.HeaderDto;
import org.example.common.dto.MessageAction;
import org.example.common.dto.MessageDto;
import org.example.server.model.request.CreateVoteRequest;
import org.example.server.model.request.VoteRequest;
import org.example.server.service.message_processing.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class MessageDispatcher {

    private static final String SERVER_USERNAME = "__server__";

    private final CreateTopicService createTopicService;
    private final GetInfoService getInfoService;
    private final CreateVoteService createVoteService;
    private final VoteService voteService;
    private final DeleteVoteService deleteVoteService;

    public MessageDto dispatch(MessageDto message) {
        log.info("Receive message {}", message);
        if (message.getHeader().getUser() == null || message.getHeader().getUser().isBlank()) {
            log.error("User unauthorised");
            return new MessageDto(new HeaderDto(SERVER_USERNAME, MessageAction.UNAUTHORISED), new HashMap<>());
        }

        try {
            return switch (message.getHeader().getAction()) {
                case CREATE_TOPIC -> {
                    createTopicService.createTopic(message);
                    yield new MessageDto(new HeaderDto(SERVER_USERNAME, MessageAction.OK), new HashMap<>());
                }

                case VIEW -> {
                    List<String> result;
                    if (!message.getPayload().containsKey("topic")) {
                        result = getInfoService.getTopicsInfo();
                    } else {
                        if (!message.getPayload().containsKey("vote")) {
                            result = getInfoService.getTopicVotes((String) message.getPayload().get("topic"));
                        } else {
                            result = getInfoService.getVoteInfo((String) message.getPayload().get("topic"),
                                    (String) message.getPayload().get("vote"));
                        }
                    }

                    yield new MessageDto(new HeaderDto(SERVER_USERNAME, MessageAction.OK), Map.of("result", result));
                }

                case CREATE_VOTE -> {
                    CreateVoteRequest request = new CreateVoteRequest(message.getPayload());
                    createVoteService.create(request, message.getHeader().getUser());
                    yield new MessageDto(new HeaderDto(SERVER_USERNAME, MessageAction.OK), new HashMap<>());
                }

                case VOTE -> {
                    VoteRequest request = new VoteRequest(message.getPayload());
                    voteService.vote(request, message.getHeader().getUser());
                    yield new MessageDto(new HeaderDto(SERVER_USERNAME, MessageAction.OK), new HashMap<>());
                }

                case DELETE -> {
                    deleteVoteService.delete(message);
                    yield new MessageDto(new HeaderDto(SERVER_USERNAME, MessageAction.OK), new HashMap<>());
                }

                case GET_VOTE -> {
                    List<String> questions = getInfoService.getVoteQuestions(message);
                    yield new MessageDto(new HeaderDto(SERVER_USERNAME, MessageAction.OK), Map.of("questions", questions));
                }

                default -> throw new RuntimeException("Unknown action");
            };
        } catch (Exception e) {
            log.error("Handle exception", e);
            return new MessageDto(new HeaderDto(SERVER_USERNAME, MessageAction.ERROR), Map.of("message", e.getMessage()));
        }
    }

}
