package org.example.server.service;

import lombok.extern.slf4j.Slf4j;
import org.example.common.parser.CommandParser;
import org.example.common.service.AbstractConsoleService;

import java.util.function.Consumer;

@Slf4j
public class ConsoleService extends AbstractConsoleService {

    private static final String LOAD = "load";
    private static final String SAVE = "save";
    private static final String EXIT = "exit";

    private final LoadService loadService;
    private final SaveService saveService;

    public ConsoleService(CommandParser commandParser,
                          LoadService loadService,
                          SaveService saveService) {
        super(commandParser);

        this.loadService = loadService;
        this.saveService = saveService;
    }

    @Override
    protected boolean proceedCommand(CommandParser.Result command) {
        try {
            log.debug("Parsed command: {}", command);
            switch (command.getCommand().split(" ")[0]) {
                case LOAD -> executeWith2LengthAssert(command.getCommand(), (args) -> loadService.load(args[1]));
                case SAVE -> executeWith2LengthAssert(command.getCommand(), (args) -> saveService.save(args[1]));
                case EXIT -> {
                    return true;
                }
                default -> System.out.println("Unknown command");
            }
        } catch (IllegalArgumentException e) {
            log.error("Handle exception", e);
            System.out.println(e.getMessage());
        }

        return false;
    }

    private void executeWith2LengthAssert(String command, Consumer<String[]> consumer) {
        String[] args = command.split(" ");
        if (args.length != 2) {
            throw new IllegalArgumentException("Command must contains 2 words");
        }
        consumer.accept(args);
    }

}
