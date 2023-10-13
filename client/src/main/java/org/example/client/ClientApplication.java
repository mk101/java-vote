package org.example.client;

import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.client.client.SocketClient;
import org.example.client.config.LoginContext;
import org.example.client.service.ConsoleService;
import org.example.common.context.ApplicationContext;
import org.example.common.parser.CommandParser;
import org.slf4j.LoggerFactory;

public class ClientApplication {

    private static final String PROPERTY_FILE = "client.properties";

    public void launch() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.stop();

        printLogo();
        ApplicationContext context = ApplicationContext.initialize(PROPERTY_FILE);
        initializeContext(context);

        context.<ConsoleService>getBean("consoleService").acceptCommand();
    }

    private void initializeContext(ApplicationContext context) {
        context.addBean("login", new LoginContext());
        context.addBean("objectMapper", new ObjectMapper());
        context.addBean("commandParser", new CommandParser());

        context.addBean("client", new SocketClient(context.getBean("objectMapper"), context.getBean("login")));
        context.addBean("consoleService", new ConsoleService(
                context.getBean("commandParser"), context.getBean("client"), context.getBean("login")));
    }

    private void printLogo() {
        System.out.println("    _________            __ ");
        System.out.println("   / ____/ (_)__  ____  / /_");
        System.out.println("  / /   / / / _ \\/ __ \\/ __/");
        System.out.println(" / /___/ / /  __/ / / / /_  ");
        System.out.println(" \\____/_/_/\\___/_/ /_/\\__/  ");
    }

}
