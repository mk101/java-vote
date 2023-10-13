package org.example.client.service;

import org.example.client.client.Client;
import org.example.client.config.LoginContext;
import org.example.client.model.CreateVoteBody;
import org.example.common.dto.MessageAction;
import org.example.common.dto.MessageDto;
import org.example.common.parser.CommandParser;
import org.example.common.service.AbstractConsoleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleService extends AbstractConsoleService {

    private static final String LOGIN = "login";
    private static final String CREATE_TOPIC = "create topic";
    private static final String VIEW = "view";
    private static final String CREATE_VOTE = "create vote";
    private static final String VOTE = "vote";
    private static final String DELETE = "delete";
    private static final String EXIT = "exit";

    private final Client client;
    private final LoginContext loginContext;

    public ConsoleService(CommandParser commandParser,
                          Client client,
                          LoginContext loginContext) {
        super(commandParser);

        this.client = client;
        this.loginContext = loginContext;
    }

    @Override
    protected boolean proceedCommand(CommandParser.Result command) {
        try {
            switch (command.getCommand()) {
                case LOGIN -> proceedLogin(command);
                case CREATE_TOPIC -> proceedCreateTopic(command);
                case VIEW -> proceedView(command);
                case CREATE_VOTE -> proceedCreateVote(command);
                case VOTE -> proceedVote(command);
                case DELETE -> proceedDelete(command);
                case EXIT -> {
                    return true;
                }

                default -> System.out.println("Unknown command");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    private void proceedLogin(CommandParser.Result command) {
        checkRequiredParameters(command, "u");

        String username = command.getOptions().get("u");
        loginContext.setLogin(username);
    }

    private void proceedCreateTopic(CommandParser.Result command) {
        checkRequiredParameters(command, "n");

        String name = command.getOptions().get("n");
        MessageDto result = client.createTopic(name);
        checkResponse(result);
    }

    private void proceedView(CommandParser.Result command) {
        String topic = command.getOptions().get("t");
        String vote = command.getOptions().get("v");

        if (topic == null && vote != null) {
            throw new IllegalArgumentException("Missing required parameter 't'");
        }

        if (topic == null) {
            printResult(client.view());
            return;
        }

        if (vote == null) {
            printResult(client.view(topic));
            return;
        }

        printResult(client.view(topic, vote));
    }

    private void proceedCreateVote(CommandParser.Result command) {
        checkRequiredParameters(command, "t");
        String topic = command.getOptions().get("t");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        if (name.isBlank()) {
            throw new IllegalStateException("Name is empty");
        }

        System.out.print("Description: ");
        String desc = scanner.nextLine().trim();
        if (desc.isBlank()) {
            throw new IllegalStateException("Description is empty");
        }

        System.out.print("Number of questions: ");
        int num = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter questions");
        List<String> questions = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            System.out.printf("%d) ", i+1);
            String question = scanner.nextLine().trim();
            if (question.isBlank()) {
                throw new IllegalStateException("Question is empty");
            }
            if (questions.contains(question)) {
                throw new IllegalStateException("This question is already on the list");
            }
            questions.add(question);
        }

        checkResponse(client.createVote(new CreateVoteBody(topic, name, desc, questions)));
    }

    @SuppressWarnings("unchecked")
    private void proceedVote(CommandParser.Result command) {
        checkRequiredParameters(command, "t", "v");
        String topic = command.getOptions().get("t");
        String vote = command.getOptions().get("v");

        Scanner scanner = new Scanner(System.in);

        MessageDto response = client.getQuestions(topic, vote);
        if (checkResponse(response)) {
            return;
        }
        List<String> questions = (List<String>) response.getPayload().get("questions");
        for (int i = 0; i < questions.size(); i++) {
            System.out.printf("%d) %s\n", i+1, questions.get(i));
        }
        System.out.println("Enter number: ");
        int num = Integer.parseInt(scanner.nextLine());
        if (num <= 0 || num > questions.size()) {
            throw new IllegalStateException("Number is out of range");
        }

        checkResponse(client.vote(topic, vote, questions.get(num - 1)));
    }

    private void proceedDelete(CommandParser.Result command) {
        checkRequiredParameters(command, "t", "v");
        String topic = command.getOptions().get("t");
        String vote = command.getOptions().get("v");

        checkResponse(client.delete(topic, vote));
    }

    private void checkRequiredParameters(CommandParser.Result command, String... parameters) {
        for (String parameter : parameters) {
            if (!command.getOptions().containsKey(parameter)) {
                throw new IllegalArgumentException(String.format("Missing required parameter '%s'", parameter));
            }
        }
    }

    private boolean checkResponse(MessageDto response) {
        if (response.getHeader().getAction() == MessageAction.ERROR) {
            System.out.println(response.getPayload().get("message"));
            return true;
        } else if (response.getHeader().getAction() == MessageAction.UNAUTHORISED) {
            System.out.println("Unauthorised");
            return true;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private void printResult(MessageDto response) {
        if (checkResponse(response)) {
            return;
        }

        List<String> result = (List<String>) response.getPayload().get("result");
        for (String line : result) {
            System.out.println(line);
        }
    }

}
