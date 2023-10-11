package org.example.launcher;

import org.example.client.ClientApplication;
import org.example.server.ServerApplication;

public class Launcher {

    private static final String SERVER = "server";
    private static final String CLIENT = "client";

    public static void main(String[] args) {
        if (args.length != 1) {
            printError();
            return;
        }

        String type = args[0];

        if (type.equalsIgnoreCase(SERVER)) {
            new ServerApplication().launch();
        } else if (type.equalsIgnoreCase(CLIENT)) {
            new ClientApplication().launch();
        } else {
            printError();
        }
    }

    private static void printError() {
        System.out.println("Error: required launch type (server or client)");
    }

}
