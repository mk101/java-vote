package org.example.client.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.client.config.LoginContext;
import org.example.client.model.CreateVoteBody;
import org.example.common.context.ApplicationContext;
import org.example.common.dto.HeaderDto;
import org.example.common.dto.MessageAction;
import org.example.common.dto.MessageDto;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class SocketClient implements Client {

    private final ObjectMapper objectMapper;
    private final LoginContext loginContext;

    private final String host;
    private final int port;

    public SocketClient(ObjectMapper objectMapper, LoginContext loginContext) {
        this.objectMapper = objectMapper;
        this.loginContext = loginContext;

        this.host = ApplicationContext.get().getProperty("server.host");
        this.port = Integer.parseInt(ApplicationContext.get().getProperty("server.port"));
    }

    @Override
    public MessageDto createTopic(String name) {
        MessageDto request = new MessageDto(
                new HeaderDto(loginContext.getLogin(), MessageAction.CREATE_TOPIC),
                Map.of("name", name));

        return sendMessage(request);
    }

    @Override
    public MessageDto view() {
        MessageDto request = new MessageDto(new HeaderDto(loginContext.getLogin(), MessageAction.VIEW), Map.of());
        return sendMessage(request);
    }

    @Override
    public MessageDto view(String topic) {
        MessageDto request = new MessageDto(new HeaderDto(loginContext.getLogin(), MessageAction.VIEW),
                Map.of("topic", topic));
        return sendMessage(request);
    }

    @Override
    public MessageDto view(String topic, String vote) {
        MessageDto request = new MessageDto(new HeaderDto(loginContext.getLogin(), MessageAction.VIEW),
                Map.of("topic", topic, "vote", vote));
        return sendMessage(request);
    }

    @Override
    public MessageDto createVote(CreateVoteBody body) {
        MessageDto request = new MessageDto(new HeaderDto(loginContext.getLogin(), MessageAction.CREATE_VOTE),
                Map.of("topic", body.getTopic(),
                        "name", body.getName(),
                        "description", body.getDescription(),
                        "answers", body.getAnswers()));
        return sendMessage(request);
    }

    @Override
    public MessageDto vote(String topic, String vote, String choose) {
        MessageDto request = new MessageDto(new HeaderDto(loginContext.getLogin(), MessageAction.VOTE),
                Map.of("topic", topic,
                        "vote", vote,
                        "choose", choose));
        return sendMessage(request);
    }

    @Override
    public MessageDto delete(String topic, String vote) {
        MessageDto request = new MessageDto(new HeaderDto(loginContext.getLogin(), MessageAction.DELETE),
                Map.of("topic", topic,
                        "vote", vote));
        return sendMessage(request);
    }

    @Override
    public MessageDto getQuestions(String topic, String vote) {
        MessageDto request = new MessageDto(new HeaderDto(loginContext.getLogin(), MessageAction.GET_VOTE),
                Map.of("topic", topic,
                        "vote", vote));
        return sendMessage(request);
    }

    private MessageDto sendMessage(MessageDto request) {
        try (Socket socket = new Socket(host, port)) {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());

            output.writeUTF(objectMapper.writeValueAsString(request));
            output.flush();

            MessageDto response = objectMapper.readValue(input.readUTF(), MessageDto.class);

            output.close();
            input.close();

            return response;
        } catch (IOException e) {
            return new MessageDto(new HeaderDto(null, MessageAction.ERROR), Map.of("message", e.getMessage()));
        }
    }

}
