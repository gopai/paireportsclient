package com.gopai.cli;

import com.gopai.pair.sdk.v1.PAIClient;
import com.gopai.data.ReportIdentifier;
import com.gopai.data.ReportIdentifierRetriever;
import com.gopai.data.SelectableReportIdentifiers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportIdentifierTerminal {
    PAIClient client;
    BufferedReader reader;
    SelectableReportIdentifiers identifiers;

    public ReportIdentifierTerminal(BufferedReader reader, PAIClient client) throws IOException {
        this.client = client;
        this.reader = reader;
        identifiers = new SelectableReportIdentifiers(new ReportIdentifierRetriever(client).findAllConfigs());
    }

    public void userSelectMultipleOptions() throws IOException {
        String input = "list";
        while (!input.equalsIgnoreCase("done")) {

            if (input.equalsIgnoreCase("list")) {
                printHeadersWithSelectionBox();
                printOptionsWithSelectionBox();
            }

            System.out.println("Select index, enter List, or enter Done: ");
            input = reader.readLine();
            identifiers.select(parseIndexes(input));
        }
        printOptionsFormattedForJar();
    }

    public void userSelectSingleOption() throws IOException {
        String input = "list";
        while (identifiers.getSelected().size() <= 0) {

            if (input.equalsIgnoreCase("list")) {
                printHeaders();
                printOptions();
            }

            System.out.println("Select index or enter List: ");
            input = reader.readLine();
            identifiers.select(parseIndexes(input));
        }
    }

    public void printOptionsFormattedForJar() {
        for (ReportIdentifier selectedOption : identifiers.getSelectedOptions()) {
            System.out.printf("java -jar PAIReportDownloader.jar -u Username -p \"password\" -guid %s -o%n", selectedOption.getReportGUID());
        }
    }

    private void printHeaders() {
        System.out.printf("%-5s | %-40.40s | %-40s |%n", "Index", "Name", "GUID");
        for (int i = 0; i < 114; i++) {
            System.out.print('-');
        }
        System.out.println();
    }

    private void printHeadersWithSelectionBox() {
        System.out.printf("%10s | %-10s | %-40.40s | %-40s |%n", "Selected", "Index", "Name", "GUID");
        for (int i = 0; i < 114; i++) {
            System.out.print('-');
        }
        System.out.println();
    }

    private void printOptions() {
        List<ReportIdentifier> options = identifiers.getOptions();
        for (int i = 0; i < options.size(); i++) {
            ReportIdentifier config = options.get(i);
            System.out.printf("%-5d | %-40.40s | %-40s |%n", i, config.getName(), config.getReportGUID());
        }
    }

    private void printOptionsWithSelectionBox() {
        List<ReportIdentifier> options = identifiers.getOptions();
        for (int i = 0; i < options.size(); i++) {
            ReportIdentifier option = options.get(i);
            System.out.printf("%10s | %-10d | %-40.40s | %-40s |%n", identifiers.isSelected(i) ? "[X]" : "[ ]", i, option.getName(), option.getReportGUID());
        }
    }

    private ArrayList<Integer> parseIndexes(String input) {
        String[] strIndexes = input.split(",|[ ]+");
        ArrayList<Integer> intIndexes = new ArrayList<>();
        for (String a : strIndexes) {
            try {
                Integer i = Integer.parseInt(a);
                if (i > -1 && i < identifiers.getOptions().size())
                    intIndexes.add(Integer.parseInt(a));
            } catch (NumberFormatException e) {
            }
        }
        return intIndexes;
    }
}
