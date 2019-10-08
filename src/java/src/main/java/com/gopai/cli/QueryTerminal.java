package com.gopai.cli;

import com.gopai.pair.sdk.v1.PAIClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

public class QueryTerminal {

    BufferedReader reader;
    PAIClient client;

    public QueryTerminal(BufferedReader reader, PAIClient client) {
        this.reader = reader;
        this.client = client;
    }

    public void startRunQueryDialog() throws IOException {
        System.out.println("You may write simple single line sql queries with the table aliased or enter quit to exit :");
        String inputText = "";
        while (!inputText.equalsIgnoreCase("quit")) {
            inputText = reader.readLine();
            if (!inputText.equalsIgnoreCase("quit")) {
                runQuery(client, inputText);
            }
        }
    }

    public void runQuery(PAIClient client, String inputText) {
        try {
            InputStream stream = client.runQuery(inputText);
            TerminalUtils.printResultsToScreen(stream);
        } catch (IOException e) {
            System.out.println("The query failed for some reason. Try another query or enter Quit.");
        }
    }

}
