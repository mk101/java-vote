package org.example.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.common.context.ApplicationContext;
import org.example.common.parser.CommandParser;
import org.example.server.dispatcher.MessageDispatcher;
import org.example.server.repository.FileRepository;
import org.example.server.service.ConsoleService;
import org.example.server.service.LoadService;
import org.example.server.service.SaveService;
import org.example.server.service.LaunchService;
import org.example.server.service.message_processing.*;

@Slf4j
public class ServerApplication {

    private static final String PROPERTY_FILE = "server.properties";

    public void launch() {
        printLogo();
        ApplicationContext context = ApplicationContext.initialize(PROPERTY_FILE);
        initializeContext(context);

        context.<LaunchService>getBean("launchService").launchServer();
        context.<ConsoleService>getBean("consoleService").acceptCommand();

        System.exit(0);
    }

    private void initializeContext(ApplicationContext context) {
        log.debug("Start initialize context");
        context.addBean("repository", new FileRepository());
        context.addBean("objectMapper", new ObjectMapper());

        context.addBean("launchService", new LaunchService());
        context.addBean("commandParser", new CommandParser());

        context.addBean("saveService", new SaveService(
                context.getBean("repository"), context.getBean("objectMapper")));
        context.addBean("loadService", new LoadService(
                context.getBean("repository"), context.getBean("objectMapper")));

        context.addBean("consoleService", new ConsoleService(
                context.getBean("commandParser"), context.getBean("loadService"),
                context.getBean("saveService")));

        context.addBean("createTopicService", new CreateTopicService(context.getBean("repository")));
        context.addBean("getInfoService", new GetInfoService(context.getBean("repository")));
        context.addBean("createVoteService", new CreateVoteService(context.getBean("repository")));
        context.addBean("voteService", new VoteService(context.getBean("repository")));
        context.addBean("deleteVoteService", new DeleteVoteService(context.getBean("repository")));

        context.addBean("messageDispatcher", new MessageDispatcher(
                context.getBean("createTopicService"), context.getBean("getInfoService"),
                context.getBean("createVoteService"), context.getBean("voteService"),
                context.getBean("deleteVoteService")));

        log.debug("Context has been initialized");
    }

    private void printLogo() {
        System.out.println("   _____                          ");
        System.out.println("  / ___/___  ______   _____  _____");
        System.out.println("  \\__ \\/ _ \\/ ___/ | / / _ \\/ ___/");
        System.out.println(" ___/ /  __/ /   | |/ /  __/ /    ");
        System.out.println("/____/\\___/_/    |___/\\___/_/     ");
        System.out.println();
    }

}
