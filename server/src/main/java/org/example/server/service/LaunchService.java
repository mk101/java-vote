package org.example.server.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.common.context.ApplicationContext;
import org.example.server.runnable.ConnectionRunnable;


@Slf4j
public class LaunchService {

    private final ApplicationContext applicationContext;

    public LaunchService() {
        this.applicationContext = ApplicationContext.get();
    }

    public void launchServer() {
        String host = applicationContext.getProperty("server.host");
        int port = Integer.parseInt(applicationContext.getProperty("server.port"));
        int maxClients = Integer.parseInt(applicationContext.getProperty("server.max-clients"));

        log.info("Start server at {}:{}", host, port);
        loop(host, port, maxClients);
    }

    @SneakyThrows
    private void loop(String host, int port, int maxClients) {
        Thread thread = new Thread(new ConnectionRunnable(host, port, maxClients));
        thread.setName("connection-thread");
        thread.setDaemon(true);
        thread.start();
    }

}
