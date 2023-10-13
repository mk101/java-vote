package org.example.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.parser.CommandParser;

import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractConsoleService {

    private final CommandParser commandParser;

    public void acceptCommand() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("#> ");
            String line = scanner.nextLine();
            log.debug("User enter: {}", line);
            try {
                if (proceedCommand(commandParser.parse(line))) {
                    break;
                }
            } catch (Exception e) {
                log.error("Failed to parse command", e);
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Proceed input command
     *
     * @param command command
     * @return true, if typed exit command
     */
    protected abstract boolean proceedCommand(CommandParser.Result command);

}
