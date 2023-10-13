package org.example.server.runnable;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.common.dto.MessageDto;
import org.example.server.dispatcher.MessageDispatcher;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

@Slf4j
@RequiredArgsConstructor
public class ConnectionHandler implements Runnable {

    private final Socket socket;

    private final MessageDispatcher messageDispatcher;

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void run() {
        log.info("Connected from {}:{}", socket.getInetAddress().getHostName(), socket.getPort());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        DataInputStream input = new DataInputStream(socket.getInputStream());

        String rawMessage = input.readUTF();
        log.debug("Receive {}", rawMessage);

        MessageDto request = objectMapper.readValue(rawMessage, MessageDto.class);
        MessageDto result = messageDispatcher.dispatch(request);

        output.writeUTF(objectMapper.writeValueAsString(result));
        output.flush();

        socket.close();
        log.info("Close connection");
    }

}
