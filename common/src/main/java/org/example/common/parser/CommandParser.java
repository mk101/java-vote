package org.example.common.parser;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandParser {

    public Result parse(String line) {
        List<String> command = new ArrayList<>();
        Map<String, String> options = new HashMap<>();

        String[] args = line.split(" ");
        for (String arg : args) {
            if (!arg.startsWith("-")) {
                command.add(arg);
                continue;
            }

            String[] option = arg.split("=");
            if (option.length != 2) {
                throw new IllegalArgumentException("The option should be in the form -<key>=<value>");
            }
            options.put(option[0].replaceFirst("-", ""), option[1]);
        }

        return new Result(String.join(" ", command), options);
    }


    @Data
    @AllArgsConstructor
    public static class Result {

        private String command;
        private Map<String, String> options;

    }

}
