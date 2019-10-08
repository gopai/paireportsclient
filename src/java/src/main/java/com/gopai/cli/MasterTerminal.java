package com.gopai.cli;

import com.gopai.pair.sdk.v1.PAIClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class MasterTerminal {

    public static void startTerminalRequireLogin(PAIClient client) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        client = TerminalUtils.startClientConnectionDialog(reader, client);
        startTerminal(client);
    }

    public static void startTerminal(PAIClient client) throws IOException {
        while (client != null && client.isConnected()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String inputText;
            inputText = startTerminalSelectionDialog(reader);
            if (inputText.equalsIgnoreCase("quit"))
                break;
            else if (inputText.toUpperCase().charAt(0) == 'Q')
                new QueryTerminal(reader, client).startRunQueryDialog();
            else if (inputText.toUpperCase().charAt(0) == 'R')
                new ReportTerminal(reader, client).startReportDialog();
            else if (inputText.toUpperCase().charAt(0) == 'J')
                new ReportIdentifierTerminal(reader, client).userSelectMultipleOptions();
        }
        System.out.println("Thanks for playing.");
    }

    public static String startTerminalSelectionDialog(BufferedReader reader) throws IOException {
        List<String> validInputs = Arrays.asList("QUERY", "Q", "REPORT", "R", "JAR", "J", "QUIT");
        String inputText = "";
        System.out.println("Query allows you to run simple Queries against our database.");
        System.out.println("Report allows you to pick and run Reports with filtering.");
        System.out.println("JAR will help you retrieve Report GUIDS and build your JAR Signature.");
        while (!validInputs.contains(inputText.toUpperCase())) {
            System.out.println("Enter Q(uery), R(eport), J(AR), or Quit");
            inputText = reader.readLine();
        }
        return inputText;
    }
}
