package org.example.server.service;

import org.example.common.parser.CommandParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.mockito.Mockito.*;

public class ConsoleServiceTests {

    private static LoadService loadService;
    private static SaveService saveService;

    private static ConsoleService service;

    @BeforeAll
    public static void init() {
        CommandParser commandParser = mock(CommandParser.class);
        loadService = mock(LoadService.class);
        saveService = mock(SaveService.class);

        service = new ConsoleService(
                commandParser, loadService, saveService);

        doNothing().when(loadService).load(any());
        doNothing().when(saveService).save(any());
    }

    @Test
    public void proceedLoad() {
        String line = "load t.txt";

        service.proceedCommand(new CommandParser.Result(line, new HashMap<>()));

        verify(loadService, times(1)).load("t.txt");
    }

    @Test
    public void proceedSave() {
        String line = "save t.txt";

        service.proceedCommand(new CommandParser.Result(line, new HashMap<>()));

        verify(saveService, times(1)).save("t.txt");
    }

    @Test
    public void proceedExit() {
        String line = "exit";

        service.proceedCommand(new CommandParser.Result(line, new HashMap<>()));

        verify(saveService, times(0)).save(any());
        verify(loadService, times(0)).load(any());
    }

    @Test
    public void wrongCommandFormat() {
        String line = "save";

        service.proceedCommand(new CommandParser.Result(line, new HashMap<>()));

        verify(saveService, times(0)).save(any());
    }

}
