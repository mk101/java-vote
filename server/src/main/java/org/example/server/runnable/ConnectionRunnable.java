package org.example.server.runnable;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.common.context.ApplicationContext;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@RequiredArgsConstructor
public class ConnectionRunnable implements Runnable {

    private final String host;
    private final int port;
    private final int maxClients;

    @Override
    @SneakyThrows
    public void run() {
        Executor executor = Executors.newFixedThreadPool(maxClients);
        try (ServerSocket socket = new ServerSocket(port, maxClients, InetAddress.getByName(host))) {
            while (true) {
                Socket connection = socket.accept();
                executor.execute(new ConnectionHandler(connection,
                        ApplicationContext.get().getBean("messageDispatcher"),
                        ApplicationContext.get().getBean("objectMapper")));
            }
        }
    }

}
