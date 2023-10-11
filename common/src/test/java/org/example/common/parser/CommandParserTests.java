package org.example.common.parser;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommandParserTests {

    @Test
    public void parseCommands() {
        CommandParser cp = new CommandParser();
        String line = "test multi command";

        CommandParser.Result result = cp.parse(line);

        assertThat(result.getCommand()).isEqualTo(line);
        assertThat(result.getOptions()).isEmpty();
    }

    @Test
    public void parseWithCommandsAndOptions() {
        CommandParser cp = new CommandParser();
        String line = "test -n=test";

        CommandParser.Result result = cp.parse(line);

        assertThat(result.getCommand()).isEqualTo("test");
        assertThat(result.getOptions()).isEqualTo(Map.of("n", "test"));
    }

    @Test
    public void parseOptions() {
        CommandParser cp = new CommandParser();
        String line = "-n=test -w=2";

        CommandParser.Result result = cp.parse(line);

        assertThat(result.getCommand()).isEmpty();
        assertThat(result.getOptions()).isEqualTo(Map.of("n", "test", "w", "2"));
    }

    @Test
    public void wrongOption() {
        CommandParser cp = new CommandParser();
        String line = "test -n";

        assertThrows(IllegalArgumentException.class, () -> cp.parse(line));
    }

}
